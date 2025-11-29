# Relatório de Correções - Projeto WalkDog

## Informações do Projeto Appwrite

O projeto **WalkDog** está configurado no servidor Appwrite com os seguintes detalhes:

- **Endpoint**: `https://appwrite.hugetower.cloud/v1`
- **Project ID**: `691e407c0036fe1c7f17`
- **Database ID**: `69236f45003447bc5844` (walkdogDB)

### Coleções Configuradas

A base de dados **walkdogDB** contém duas coleções principais:

#### 1. Coleção "fornecedor"
- **Collection ID**: `69236f93001828d82b6f`
- **Atributos** (7 campos):
  - `nome` (String, Required)
  - `morada` (String[], Optional)
  - `codpostal` (Integer, Optional)
  - `localidade` (String, Optional)
  - `nif` (Integer, Required)
  - `email` (Email, Required)
  - `senha` (String, Required)

#### 2. Coleção "cliente"
- **Collection ID**: `69236f5200282814eb3c`
- **Atributos** (10 campos):
  - `nome` (String, Required)
  - `email` (Email, Required)
  - `senha` (String, Required)
  - `localizacao` (String, Required)
  - `endereco` (String, Required)
  - `numeroCartao` (String, Required)
  - `validade` (String, Required)
  - `cvv` (String, Required)
  - `iban` (String, Required)
  - `fotoId` (String, Optional)

---

## Erros Identificados e Corrigidos

### ✅ ERRO 1: Dependência do SDK Appwrite em Falta

**Problema**: O ficheiro `app/build.gradle.kts` não incluía a dependência do Appwrite SDK, impedindo a compilação do projeto.

**Correção Aplicada**:
```kotlin
// --- APPWRITE SDK ---
implementation("io.appwrite:sdk-for-android:6.0.0")
```

**Ficheiro**: `/app/build.gradle.kts` (linha 79-80)

---

### ✅ ERRO 2: Conflito de Imports no AppwriteService

**Problema**: Import incorreto de `androidx.datastore.core.Storage` em vez de `io.appwrite.services.Storage`.

**Correção Aplicada**: Removida a linha de import conflituosa.

**Antes**:
```kotlin
import androidx.datastore.core.Storage
import io.appwrite.services.Storage
```

**Depois**:
```kotlin
import io.appwrite.services.Storage
```

**Ficheiro**: `/app/src/main/java/com/example/walkdog/service/AppwriteService.kt`

---

### ✅ ERRO 3: Configuração setSelfSigned Desnecessária

**Problema**: O código usava `.setSelfSigned(true)` que é apenas para desenvolvimento com certificados SSL inválidos.

**Correção Aplicada**: Removida a linha `.setSelfSigned(true)` pois o servidor tem SSL válido.

**Antes**:
```kotlin
client = Client(context)
    .setEndpoint("https://appwrite.hugetower.cloud/v1")
    .setProject("691e407c0036fe1c7f17")
    .setSelfSigned(true)
```

**Depois**:
```kotlin
client = Client(context)
    .setEndpoint("https://appwrite.hugetower.cloud/v1")
    .setProject("691e407c0036fe1c7f17")
```

**Ficheiro**: `/app/src/main/java/com/example/walkdog/service/AppwriteService.kt`

---

### ✅ ERRO 4: IDs da Base de Dados Incorretos (PerfilClienteViewModel)

**Problema**: O código usava nomes textuais em vez dos IDs reais das coleções do Appwrite.

**Correção Aplicada**:

**Antes**:
```kotlin
private val DB_ID = "walkdog"
private val COLLECTION_CLIENTES = "clientes"
private val COLLECTION_FORNECEDORES = "fornecedores"
```

**Depois**:
```kotlin
private val DB_ID = "69236f45003447bc5844" // ID da base de dados walkdogDB
private val COLLECTION_CLIENTES = "69236f5200282814eb3c" // ID da coleção cliente
private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f" // ID da coleção fornecedor
```

**Ficheiro**: `/app/src/main/java/com/example/walkdog/viewmodel/PerfilClienteViewModel.kt`

---

### ✅ ERRO 5: IDs da Base de Dados Incorretos (PerfilFornecedorViewModel)

**Problema**: Mesmo erro do ViewModel anterior - IDs incorretos.

**Correção Aplicada**:

**Antes**:
```kotlin
private val DB_ID = "walkdog"
private val COLLECTION_FORNECEDORES = "fornecedores"
private val COLLECTION_CLIENTES = "clientes"
```

**Depois**:
```kotlin
private val DB_ID = "69236f45003447bc5844" // ID da base de dados walkdogDB
private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f" // ID da coleção fornecedor
private val COLLECTION_CLIENTES = "69236f5200282814eb3c" // ID da coleção cliente
```

**Ficheiro**: `/app/src/main/java/com/example/walkdog/viewmodel/PerfilFornecedorViewModel.kt`

---

## Resumo das Alterações

| Ficheiro | Tipo de Alteração | Descrição |
|----------|------------------|-----------|
| `app/build.gradle.kts` | Adição | Dependência do Appwrite SDK v6.0.0 |
| `AppwriteService.kt` | Remoção | Import conflituoso do androidx.datastore |
| `AppwriteService.kt` | Remoção | Configuração setSelfSigned(true) |
| `PerfilClienteViewModel.kt` | Correção | IDs corretos da base de dados e coleções |
| `PerfilFornecedorViewModel.kt` | Correção | IDs corretos da base de dados e coleções |

---

## Próximos Passos

Para utilizar o projeto corrigido:

1. **Extrair o ficheiro** `WalkDog_DB_Corrigido.zip`
2. **Abrir o projeto** no Android Studio
3. **Sincronizar o Gradle** (Sync Project with Gradle Files)
4. **Compilar o projeto** para verificar se não há erros
5. **Testar a conexão** com o Appwrite executando a aplicação

### Verificação da Conexão

A inicialização do Appwrite está corretamente configurada no `MainActivity.onCreate()`:

```kotlin
AppwriteService.init(this)
```

Isto garante que o cliente Appwrite é inicializado antes de qualquer operação de base de dados.

---

## Notas Importantes

⚠️ **Autenticação**: O projeto usa o sistema de autenticação do Appwrite (`Account.createEmailPasswordSession`). Certifique-se de que:
- Os utilizadores estão registados na secção **Auth** do console Appwrite
- As permissões das coleções permitem leitura/escrita dos documentos

⚠️ **Permissões**: Verifique as permissões das coleções no Appwrite Console para garantir que os utilizadores autenticados podem aceder aos dados.

⚠️ **Versão do SDK**: Foi utilizada a versão `6.0.0` do Appwrite SDK para Android. Verifique a compatibilidade com a versão do servidor Appwrite (atualmente 1.6.1).

---

## Contacto e Suporte

Se encontrar algum problema adicional:
1. Verifique os logs do Logcat no Android Studio
2. Consulte a documentação oficial: https://appwrite.io/docs
3. Verifique o console do Appwrite para erros de permissões

---

**Data da Correção**: 29 de Novembro de 2025  
**Versão do Appwrite Server**: 1.6.1  
**Versão do SDK Android**: 6.0.0
