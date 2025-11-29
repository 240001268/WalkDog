# 🚨 Solução: Erro de Caracteres Não-ASCII no Caminho

## ❌ O Problema

O Gradle está a falhar porque o **caminho do projeto contém caracteres especiais**:

```
C:\Users\User\StudioProjects\CORREÇÕES WALKDOG\...
                              ^^^^^^^^ ^^^^^^^
                              Ç e Õ causam erro!
```

**Mensagem de erro**:
```
Your project path contains non-ASCII characters.
This will most likely cause the build to fail on Windows.
```

O Gradle no Windows **não suporta** caracteres acentuados (Ç, Õ, Á, É, etc.) no caminho do projeto.

---

## ✅ Solução 1: Mover para Caminho Sem Acentos (RECOMENDADO)

### Passo-a-Passo:

1. **Fechar completamente o Android Studio**

2. **Criar uma nova pasta SEM acentos**
   - Exemplo: `C:\Projetos\WalkDog`
   - Ou: `C:\Users\User\Desktop\WalkDog`
   - Ou: `C:\AndroidProjects\WalkDog`

3. **Mover o projeto**
   - Copiar a pasta `WalkDog_DB` para o novo local
   - Exemplo final: `C:\Projetos\WalkDog\WalkDog_DB`

4. **Abrir no Android Studio**
   - Abrir o Android Studio
   - Clicar em "Open"
   - Selecionar: `C:\Projetos\WalkDog\WalkDog_DB`

5. **Sincronizar e compilar**
   - File → Sync Project with Gradle Files
   - Build → Rebuild Project

---

## ✅ Solução 2: Adicionar Configuração ao gradle.properties (Temporária)

Se não puder mover o projeto agora, pode **desativar a verificação** (não recomendado para produção):

1. **Abrir o ficheiro `gradle.properties`**
   - Localização: `WalkDog_DB\gradle.properties`

2. **Adicionar esta linha no final**:
   ```properties
   android.overridePathCheck=true
   ```

3. **Guardar o ficheiro**

4. **Sincronizar no Android Studio**
   - File → Sync Project with Gradle Files

⚠️ **AVISO**: Esta solução pode causar problemas futuros. É melhor usar a Solução 1.

---

## 📋 Caminhos Válidos vs Inválidos

### ✅ VÁLIDOS (sem acentos):
```
C:\Projetos\WalkDog\
C:\AndroidProjects\WalkDog\
C:\Users\User\Desktop\WalkDog\
C:\Dev\WalkDog\
D:\Projects\WalkDog\
```

### ❌ INVÁLIDOS (com acentos/caracteres especiais):
```
C:\Projetos\CORREÇÕES\          ❌ Tem Ç
C:\Projetos\Aplicações\         ❌ Tem Ç e Õ
C:\Users\José\Desktop\          ❌ Tem é
C:\Projetos\Versão 1.0\         ❌ Tem ã
```

---

## 🎯 Solução Rápida (Copiar e Colar)

Execute estes comandos no **Prompt de Comando** (CMD):

```batch
REM 1. Criar pasta sem acentos
mkdir C:\Projetos

REM 2. Mover o projeto (ajuste o caminho de origem)
xcopy "C:\Users\User\StudioProjects\CORREÇÕES WALKDOG\WalkDog_DB_Corrigido_Final\WalkDog_DB" "C:\Projetos\WalkDog_DB" /E /I /H

REM 3. Abrir a nova pasta
explorer C:\Projetos\WalkDog_DB
```

Depois abra esta nova pasta no Android Studio.

---

## 🔧 Solução Automática via Script

Criei um script que faz tudo automaticamente:

1. **Criar ficheiro `mover_projeto.bat`** com este conteúdo:

```batch
@echo off
echo ==========================================
echo  MOVER PROJETO PARA CAMINHO VALIDO
echo ==========================================
echo.

REM Definir caminhos
set "ORIGEM=%~dp0"
set "DESTINO=C:\Projetos\WalkDog_DB"

echo Origem: %ORIGEM%
echo Destino: %DESTINO%
echo.

REM Criar pasta de destino
if not exist "C:\Projetos" mkdir "C:\Projetos"

echo Copiando ficheiros...
xcopy "%ORIGEM%" "%DESTINO%" /E /I /H /Y

echo.
echo ==========================================
echo  PROJETO COPIADO COM SUCESSO!
echo ==========================================
echo.
echo Novo caminho: %DESTINO%
echo.
echo Agora abra este caminho no Android Studio.
echo.
pause

REM Abrir pasta no explorador
explorer "%DESTINO%"
```

2. **Guardar na pasta do projeto**
3. **Executar o ficheiro**
4. **Abrir o novo caminho no Android Studio**

---

## 📊 Comparação das Soluções

| Solução | Vantagem | Desvantagem | Recomendação |
|---------|----------|-------------|--------------|
| **Mover projeto** | Resolve definitivamente | Precisa mover ficheiros | ⭐⭐⭐⭐⭐ |
| **Override check** | Rápido, sem mover | Pode causar problemas | ⭐⭐ |

---

## ⚠️ Importante

- **NÃO use espaços excessivos** no caminho
- **NÃO use caracteres especiais**: Ç, Õ, Á, É, Ã, Â, etc.
- **Use apenas**: A-Z, a-z, 0-9, underscore (_), hífen (-)
- **Evite caminhos muito longos** (máximo 260 caracteres no Windows)

---

## 🎯 Resumo

**Problema**: Caminho com "Ç" e "Õ"  
**Solução**: Mover para `C:\Projetos\WalkDog_DB`  
**Resultado**: Projeto vai compilar sem erros

---

## 📞 Após Mover o Projeto

1. ✅ Abrir no Android Studio
2. ✅ File → Sync Project with Gradle Files
3. ✅ Executar `limpar_gradle_cache.bat` (se necessário)
4. ✅ Build → Rebuild Project
5. ✅ Verificar se compila sem erros

**O código está correto! É apenas o caminho que precisa ser alterado.**
