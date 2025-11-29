# Correções de Tipos do Appwrite SDK 6.0.0

## 🔍 Problema Identificado

O Appwrite SDK versão 6.0.0 para Android usa **tipos genéricos** para `User`, `Session` e `Document`. 

O código original não especificava o parâmetro de tipo genérico, causando o erro:
```
One type argument expected for class 'User'
```

---

## ✅ Correções Aplicadas

### 1. LoginViewModel.kt

#### Linha 27-28 (Função login)
**Antes**:
```kotlin
val session: Session = AppwriteService.account
    .createEmailPasswordSession(email, password)
```

**Depois**:
```kotlin
val session = AppwriteService.account
    .createEmailPasswordSession(email, password)
```
*Nota: Removido o tipo explícito, deixando o Kotlin inferir automaticamente.*

---

#### Linha 43-48 (Função register)
**Antes**:
```kotlin
val user: User = AppwriteService.account.create(
    userId = "unique()",
    email = email,
    password = password,
    name = name
)
```

**Depois**:
```kotlin
val user: User<Map<String, Any>> = AppwriteService.account.create(
    userId = "unique()",
    email = email,
    password = password,
    name = name
)
```
*Nota: Adicionado parâmetro de tipo genérico `<Map<String, Any>>`.*

---

### 2. PerfilClienteViewModel.kt

#### Linhas 10-15 (Data class)
**Antes**:
```kotlin
data class PerfilClienteUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val cliente: Document? = null,
    val fornecedores: List<Document> = emptyList()
)
```

**Depois**:
```kotlin
data class PerfilClienteUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val cliente: Document<Map<String, Any>>? = null,
    val fornecedores: List<Document<Map<String, Any>>> = emptyList()
)
```
*Nota: Adicionado `<Map<String, Any>>` a todos os tipos `Document`.*

---

### 3. PerfilFornecedorViewModel.kt

#### Linhas 10-15 (Data class)
**Antes**:
```kotlin
data class PerfilFornecedorUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val fornecedor: Document? = null,
    val clientes: List<Document> = emptyList()
)
```

**Depois**:
```kotlin
data class PerfilFornecedorUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val fornecedor: Document<Map<String, Any>>? = null,
    val clientes: List<Document<Map<String, Any>>> = emptyList()
)
```
*Nota: Adicionado `<Map<String, Any>>` a todos os tipos `Document`.*

---

## 📚 Explicação Técnica

### Por que `Map<String, Any>`?

O Appwrite SDK retorna documentos como mapas chave-valor onde:
- **Chave** (`String`): Nome do campo/atributo
- **Valor** (`Any`): Pode ser qualquer tipo (String, Int, Boolean, etc.)

Exemplo de documento:
```kotlin
{
    "nome": "João Silva",           // String
    "email": "joao@example.com",    // String
    "nif": 123456789,               // Int
    "senha": "hash..."              // String
}
```

### Acesso aos Dados

Para aceder aos campos do documento:

```kotlin
val cliente: Document<Map<String, Any>> = ...

// Aceder aos dados
val nome = cliente.data["nome"] as? String
val email = cliente.data["email"] as? String
val nif = cliente.data["nif"] as? Int
```

Ou usando o ID do documento:
```kotlin
val documentId = cliente.id
```

---

## 🎯 Resumo das Alterações

| Ficheiro | Linhas Alteradas | Tipo Corrigido |
|----------|------------------|----------------|
| LoginViewModel.kt | 27-28 | Session (inferência) |
| LoginViewModel.kt | 43 | User<Map<String, Any>> |
| PerfilClienteViewModel.kt | 13-14 | Document<Map<String, Any>> |
| PerfilFornecedorViewModel.kt | 13-14 | Document<Map<String, Any>> |

**Total**: 4 correções em 3 ficheiros

---

## ✅ Verificação

Após estas correções, o projeto deve compilar sem erros de tipo relacionados ao Appwrite SDK.

Para verificar:
```bash
# No terminal do Android Studio
./gradlew build
```

Ou no Android Studio:
- Build → Rebuild Project

---

## 📖 Referência

- [Appwrite SDK for Android - Documentação Oficial](https://appwrite.io/docs/sdks#android)
- [Kotlin Generics](https://kotlinlang.org/docs/generics.html)

---

**Data da Correção**: 29 de Novembro de 2025  
**SDK Version**: Appwrite Android SDK 6.0.0
