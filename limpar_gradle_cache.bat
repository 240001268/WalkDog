@echo off
echo ========================================
echo  SCRIPT DE LIMPEZA DO GRADLE - WALKDOG
echo ========================================
echo.
echo Este script vai resolver o erro de cache do Gradle.
echo.
pause

echo.
echo [1/5] Parando todos os processos do Gradle...
call gradlew --stop
timeout /t 2 /nobreak > nul

echo.
echo [2/5] Limpando o projeto...
call gradlew clean
timeout /t 2 /nobreak > nul

echo.
echo [3/5] Apagando cache do Gradle (pasta 8.13)...
if exist "%USERPROFILE%\.gradle\caches\8.13" (
    rmdir /s /q "%USERPROFILE%\.gradle\caches\8.13"
    echo Cache 8.13 apagada com sucesso!
) else (
    echo Cache 8.13 nao encontrada (ja foi apagada ou nao existe).
)

echo.
echo [4/5] Apagando pasta build do projeto...
if exist "app\build" (
    rmdir /s /q "app\build"
    echo Pasta build apagada com sucesso!
) else (
    echo Pasta build nao encontrada.
)

if exist "build" (
    rmdir /s /q "build"
    echo Pasta build raiz apagada com sucesso!
)

echo.
echo [5/5] Apagando pasta .gradle do projeto...
if exist ".gradle" (
    rmdir /s /q ".gradle"
    echo Pasta .gradle do projeto apagada com sucesso!
) else (
    echo Pasta .gradle nao encontrada.
)

echo.
echo ========================================
echo  LIMPEZA CONCLUIDA COM SUCESSO!
echo ========================================
echo.
echo Proximos passos:
echo 1. Abra o Android Studio
echo 2. File -^> Sync Project with Gradle Files
echo 3. Build -^> Rebuild Project
echo.
echo Se o erro persistir, execute o Android Studio como Administrador.
echo.
pause
