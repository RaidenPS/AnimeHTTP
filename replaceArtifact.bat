@echo off

REM SET ENVIRONMENT
call "..\tools\setenv.bat"

set "source=.\out\artifacts\%RAIDENHTTP_PROJ_NAME%_jar\%RAIDENHTTP_PROJ_NAME%.jar"
set "destination=.\"
move "%source%" "%destination%"

if %ERRORLEVEL% equ 0 (
    echo Done!
) else (
    echo Failed! Error Code: %ERRORLEVEL%.
)
pause