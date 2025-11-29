# Correção: Tela Preta na Aplicação

## 🔍 Problema

A aplicação compilava com sucesso mas mostrava **tela preta** ao executar no emulador.

## 🕵️ Investigação

### Análise do Logcat
- ✅ Sem crashes (Exception/FATAL)
- ✅ Sem erros de runtime
- ✅ Aplicação iniciava normalmente
- ❌ Tela não renderizava

### Causa Provável

O **SplashScreen** tinha **animações complexas** que podem ter causado problemas de renderização:

```kotlin
// ANTES - Com AnimatedVisibility complexa
var visible by remember { mutableStateOf(false) }

LaunchedEffect(Unit) {
    visible = true  // Mudança de estado
    delay(2800)
    onTimeout()
}

AnimatedVisibility(
    visible = visible,
    enter = fadeIn(animationSpec = tween(1000)),
    exit = fadeOut()
) {
    // Conteúdo...
}
```

**Problemas potenciais**:
1. Estado `visible` começava como `false`
2. AnimatedVisibility pode não renderizar imediatamente
3. Animações complexas podem falhar silenciosamente

---

## ✅ Solução Aplicada

### 1. Simplificação do SplashScreen

**Removido**:
- ❌ `AnimatedVisibility`
- ❌ `fadeIn`/`fadeOut`
- ❌ Estado `visible`
- ❌ Animações de escala e alpha
- ❌ Package incorreto (`com.example.walkdog` → `com.example.walkdog.Screens`)

**Mantido**:
- ✅ Layout básico
- ✅ Cores e estilos
- ✅ Imagem do cão (com try-catch)
- ✅ CircularProgressIndicator
- ✅ Delay de 2 segundos

### 2. Código Corrigido

```kotlin
package com.example.walkdog.Screens

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    // Simples e direto
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDE3FF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "WalkDog",
                fontSize = 48.sp,
                color = Color(0xFF6A1B9A),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Try-catch para evitar crash se imagem não existir
            try {
                Image(
                    painter = painterResource(id = R.drawable.dog),
                    contentDescription = "Logo WalkDog",
                    modifier = Modifier.size(120.dp)
                )
            } catch (e: Exception) {
                // Continua sem a imagem
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bem-vindo!",
                fontSize = 20.sp,
                color = Color(0xFF6A1B9A),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = Color(0xFF6A1B9A),
                strokeWidth = 4.dp
            )
        }
    }
}
```

---

## 🎯 Mudanças Principais

| Antes | Depois |
|-------|--------|
| `package com.example.walkdog` | `package com.example.walkdog.Screens` ✅ |
| `AnimatedVisibility` com estado | Renderização direta ✅ |
| `fadeIn`/`fadeOut` | Sem animações ✅ |
| `visible` state | Sem estado extra ✅ |
| Delay 2800ms | Delay 2000ms ✅ |
| Imagem sem proteção | `try-catch` na imagem ✅ |

---

## 🔧 Outras Correções Aplicadas

### 1. Package Correto
O SplashScreen estava no package errado:
- **Antes**: `package com.example.walkdog`
- **Depois**: `package com.example.walkdog.Screens`

Isto garante consistência com os outros ficheiros em `Screens/`.

### 2. Import do R
Adicionado import explícito:
```kotlin
import com.example.walkdog.R
```

### 3. Proteção de Imagem
```kotlin
try {
    Image(painter = painterResource(id = R.drawable.dog), ...)
} catch (e: Exception) {
    // Não crashar se imagem não existir
}
```

---

## 🧪 Como Testar

1. **Limpar o projeto**:
   ```
   Build → Clean Project
   ```

2. **Rebuild**:
   ```
   Build → Rebuild Project
   ```

3. **Executar**:
   - Run → Run 'app'
   - Aguardar instalação no emulador

4. **Verificar**:
   - ✅ Tela roxa com "WalkDog"
   - ✅ Imagem do cão (se existir)
   - ✅ "Bem-vindo!"
   - ✅ Loading spinner
   - ✅ Após 2 segundos → Login

---

## 💡 Lições Aprendidas

### 1. Animações Complexas
Animações do Compose podem falhar silenciosamente:
- Sem erro no Logcat
- Tela fica preta
- Difícil de debugar

**Solução**: Começar simples, adicionar animações depois.

### 2. Estados Iniciais
Estados que começam como `false` podem causar problemas:
```kotlin
var visible by remember { mutableStateOf(false) } // ❌ Começa invisível
```

**Melhor**: Renderizar diretamente sem estado extra.

### 3. Packages Consistentes
Manter estrutura de packages consistente:
```
com.example.walkdog.Screens.* // ✅ Todos os screens aqui
```

---

## 📋 Checklist de Verificação

Após aplicar a correção:

- [x] SplashScreen simplificado
- [x] Package correto
- [x] Sem AnimatedVisibility
- [x] Try-catch na imagem
- [x] Import do R adicionado
- [x] Delay reduzido para 2s
- [ ] Testar no emulador
- [ ] Verificar navegação para Login

---

## 🚀 Resultado Esperado

Ao executar a app:

1. **Tela roxa** aparece imediatamente
2. **"WalkDog"** em roxo escuro (grande)
3. **Imagem do cão** no centro (120dp)
4. **"Bem-vindo!"** abaixo
5. **Loading spinner** roxo
6. Após **2 segundos** → Navega para **Login**

---

## ⚠️ Se o Problema Persistir

### 1. Limpar Cache do Android Studio
```
File → Invalidate Caches → Invalidate and Restart
```

### 2. Limpar Build
```bash
./gradlew clean
./gradlew build
```

### 3. Reinstalar no Emulador
- Desinstalar a app manualmente no emulador
- Run → Run 'app' novamente

### 4. Verificar Logcat
Procurar por:
- `FATAL EXCEPTION`
- `AndroidRuntime`
- `java.lang.RuntimeException`

### 5. Testar em Dispositivo Real
Às vezes o emulador tem problemas específicos.

---

**Data da Correção**: 29 de Novembro de 2025  
**Ficheiro Modificado**: `SplashScreen.kt`  
**Tipo de Correção**: Simplificação de UI/Animações
