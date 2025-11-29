@echo off
chcp 65001 >nul
echo ==========================================
echo  MOVER PROJETO PARA CAMINHO VALIDO
echo ==========================================
echo.
echo Este script vai copiar o projeto para um
echo caminho SEM caracteres especiais (acentos).
echo.
echo IMPORTANTE: Feche o Android Studio antes!
echo.
pause

REM Definir caminhos
set "ORIGEM=%~dp0"
set "DESTINO=C:\Projetos\WalkDog_DB"

echo.
echo Origem: %ORIGEM%
echo Destino: %DESTINO%
echo.

REM Verificar se destino ja existe
if exist "%DESTINO%" (
    echo.
    echo AVISO: A pasta de destino ja existe!
    echo Deseja substituir? (S/N)
    set /p resposta=
    if /i not "%resposta%"=="S" (
        echo.
        echo Operacao cancelada.
        pause
        exit /b
    )
    echo.
    echo Apagando pasta existente...
    rmdir /s /q "%DESTINO%"
)

REM Criar pasta de destino
echo.
echo Criando pasta de destino...
if not exist "C:\Projetos" mkdir "C:\Projetos"

REM Copiar ficheiros
echo.
echo Copiando ficheiros...
echo (Isto pode demorar alguns segundos)
echo.
xcopy "%ORIGEM%*" "%DESTINO%\" /E /I /H /Y /Q

if errorlevel 1 (
    echo.
    echo ERRO ao copiar ficheiros!
    pause
    exit /b 1
)

echo.
echo ==========================================
echo  PROJETO COPIADO COM SUCESSO!
echo ==========================================
echo.
echo Novo caminho: %DESTINO%
echo.
echo Proximos passos:
echo 1. Abra o Android Studio
echo 2. Clique em "Open"
echo 3. Selecione: %DESTINO%
echo 4. Aguarde a sincronizacao
echo.
echo A pasta sera aberta no explorador...
echo.
pause

REM Abrir pasta no explorador
explorer "%DESTINO%"
