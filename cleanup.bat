@echo off
echo ----------------------------------------------------
echo Press any key to delete all files with ending:
echo  *.aps *.idb *.ncp *.obj *.pch *.tmp *.sbr *.plg
echo Visual C++/.Net junk 
echo ----------------------------------------------------
pause

del /F /Q /S /A- *.aps *.idb *.ncp *.obj *.pch *.sbr *.tmp *.pdb *.bsc *.ilk *.res *.ncb *.opt *.suo *.manifest *.dep *.plg *.user *.vsp *.o

FOR /r /d %%d IN (release,debug) DO (

  IF EXIST "%%d" (
   rd /S /Q "%%d"
   echo rmdir - "%%d"
  )
)

pause


