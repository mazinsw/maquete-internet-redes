@echo off
set CLASSPATH=Servidor/bin/;Roteador/bin/;Host/bin/;Classes/bin/
rmiregistry
if NOT %ERRORLEVEL% EQU 0 pause