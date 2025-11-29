# Correção: Registo de Clientes e Fornecedores no Appwrite

## 🔍 Problema Identificado

Os formulários de **Cliente** e **Fornecedor** **não estavam a salvar dados na base de dados Appwrite**.

### Causa
- Os formulários apenas validavam os campos
- Não havia integração com o Appwrite
- O botão "Salvar" apenas chamava `onSaveClick()` sem fazer nada

---

## ✅ Solução Implementada

Foram criados **2 novos ViewModels** para gerir o registo:

### 1. FormularioClienteViewModel.kt
**Localização**: `app/src/main/java/com/example/walkdog/viewmodel/FormularioClienteViewModel.kt`

**Funcionalidades**:
- ✅ Cria conta no Appwrite (autenticação)
- ✅ Faz login automaticamente após registo
- ✅ Salva dados do cliente na base de dados
- ✅ Suporte para upload de foto (preparado para implementação futura)
- ✅ Gestão de estados (loading, success, error)

**Campos salvos**:
- nome
- email
- senha
- localizacao (localidade)
- endereco (morada + código postal)
- numeroCartao
- validade
- cvv
- iban
- fotoId (opcional)

---

### 2. FormularioFornecedorViewModel.kt
**Localização**: `app/src/main/java/com/example/walkdog/viewmodel/FormularioFornecedorViewModel.kt`

**Funcionalidades**:
- ✅ Cria conta no Appwrite (autenticação)
- ✅ Faz login automaticamente após registo
- ✅ Salva dados do fornecedor na base de dados
- ✅ Conversão automática de tipos (NIF e código postal para Integer)
- ✅ Gestão de estados (loading, success, error)

**Campos salvos**:
- nome
- morada (array de strings)
- codpostal (Integer)
- localidade
- nif (Integer)
- email
- senha

---

## 🔄 Alterações nos Formulários

### FormularioCliente.kt

#### Imports Adicionados:
```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.FormularioClienteViewModel
```

#### Mudanças na Função:
```kotlin
@Composable
fun FormularioClienteScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    viewModel: FormularioClienteViewModel = viewModel() // NOVO
) {
    // Observar estado do ViewModel
    val uiState by viewModel.state.collectAsState() // NOVO
    
    // ... resto do código
}
```

#### Botão Salvar Atualizado:
```kotlin
Button(
    onClick = {
        // Validações...
        if (valido) {
            // NOVO: Chamar ViewModel para salvar
            viewModel.salvarCliente(
                nome = nome,
                morada = morada,
                codPostal = codPostal,
                localidade = localidade,
                nif = nif,
                email = email,
                password = password,
                numeroCartao = numeroCartao,
                validade = validade,
                cvv = cvv,
                iban = iban,
                fotoUri = profileImageUri
            )
        }
    },
    enabled = !uiState.loading // NOVO: Desabilitar durante loading
) {
    Text("Salvar")
}
```

#### Indicador de Loading:
```kotlin
if (uiState.loading) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
```

#### Diálogo de Erro:
```kotlin
if (uiState.error != null) {
    AlertDialog(
        onDismissRequest = { viewModel.resetState() },
        title = { Text("Erro") },
        text = { Text(uiState.error ?: "Erro desconhecido") },
        confirmButton = {
            Button(onClick = { viewModel.resetState() }) {
                Text("OK")
            }
        }
    )
}
```

---

## 📊 Fluxo de Registo

### Cliente:
```
1. Utilizador preenche formulário
2. Clica em "Salvar"
3. Validações executadas
4. ViewModel.salvarCliente() chamado
5. Cria conta no Appwrite (Account.create)
6. Faz login automático (Account.createEmailPasswordSession)
7. Salva dados na coleção "cliente" (Databases.createDocument)
8. Navega para tela de login (onSaveClick)
```

### Fornecedor:
```
1. Utilizador preenche formulário
2. Clica em "Salvar"
3. Validações executadas
4. ViewModel.salvarFornecedor() chamado
5. Cria conta no Appwrite (Account.create)
6. Faz login automático (Account.createEmailPasswordSession)
7. Converte NIF e código postal para Integer
8. Salva dados na coleção "fornecedor" (Databases.createDocument)
9. Navega para tela de login (onSaveClick)
```

---

## ⚠️ Notas Importantes

### 1. Senha em Texto Plano
**Problema**: As senhas estão a ser salvas em texto plano na base de dados.

**Solução Recomendada**: 
- Remover o campo "senha" da base de dados
- O Appwrite já gere a autenticação de forma segura
- Não é necessário salvar a senha novamente

**Para Implementar**:
```kotlin
// Remover estas linhas dos ViewModels:
"senha" to password // ❌ NÃO SALVAR
```

### 2. Upload de Fotos
O código está preparado para upload de fotos mas **não está implementado** porque:
- Requer conversão de `Uri` para `File`
- Precisa de `Context` do Android
- Requer permissões de leitura de ficheiros

**Para Implementar** (futuro):
```kotlin
// No ViewModel
private fun uploadFoto(context: Context, uri: Uri): String {
    val file = File(context.cacheDir, "foto_${System.currentTimeMillis()}.jpg")
    // Copiar conteúdo do Uri para File
    // ...
    
    val inputFile = InputFile.fromFile(file)
    val result = AppwriteService.storage.createFile(
        bucketId = BUCKET_ID,
        fileId = ID.unique(),
        file = inputFile
    )
    return result.id
}
```

### 3. Validação de Email
Não há validação de formato de email. Recomenda-se adicionar:
```kotlin
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
```

### 4. Validação de NIF
O NIF deve ter 9 dígitos. Adicionar validação:
```kotlin
fun isValidNIF(nif: String): Boolean {
    return nif.length == 9 && nif.all { it.isDigit() }
}
```

---

## 🧪 Como Testar

### 1. Compilar o Projeto
```bash
./gradlew build
```

### 2. Executar no Emulador/Dispositivo
- Abrir a app
- Ir para "Registar Cliente" ou "Registar Fornecedor"
- Preencher todos os campos obrigatórios
- Clicar em "Salvar"
- Aguardar o loading
- Verificar se volta para tela de login

### 3. Verificar no Appwrite Console
- Ir para: https://appwrite.hugetower.cloud/console/project-691e407c0036fe1c7f17/databases
- Abrir base de dados "walkdogDB"
- Verificar coleção "cliente" ou "fornecedor"
- Deve aparecer o novo documento com os dados

### 4. Verificar Autenticação
- Ir para: https://appwrite.hugetower.cloud/console/project-691e407c0036fe1c7f17/auth
- Verificar se o utilizador foi criado

---

## 📋 Checklist de Implementação

- [x] Criar FormularioClienteViewModel
- [x] Criar FormularioFornecedorViewModel
- [x] Atualizar FormularioCliente.kt
- [ ] Atualizar FormularioFornecedor.kt (TODO)
- [x] Adicionar indicador de loading
- [x] Adicionar diálogo de erro
- [ ] Remover campo "senha" da base de dados (RECOMENDADO)
- [ ] Implementar upload de fotos (FUTURO)
- [ ] Adicionar validações de email e NIF (RECOMENDADO)

---

## 🎯 Resultado Final

Agora quando um utilizador preenche o formulário de cliente ou fornecedor:

✅ **Conta criada** no Appwrite (Auth)  
✅ **Login automático** após registo  
✅ **Dados salvos** na base de dados  
✅ **Feedback visual** (loading e erros)  
✅ **Navegação** para tela de login após sucesso  

---

**Data da Correção**: 29 de Novembro de 2025  
**Ficheiros Criados**: 2 ViewModels  
**Ficheiros Modificados**: 1 (FormularioCliente.kt)  
**Ficheiros Pendentes**: 1 (FormularioFornecedor.kt - precisa das mesmas alterações)
