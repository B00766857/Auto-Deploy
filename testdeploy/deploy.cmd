@echo off

echo ---Deployment site


copy d:\home\site\repository\testdeploy\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war