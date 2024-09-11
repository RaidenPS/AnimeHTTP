@echo off

setlocal

if exist "./out" (
    rmdir /s /q "./out"
    echo Done!
) else (
    echo The directory does not exist.
)

endlocal
pause