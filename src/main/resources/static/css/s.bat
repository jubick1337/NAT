for /f "delims=" %%i in ('dir /b /a-d *.css') do ren "%%~i" "map_%%~ni%%~xi" 
