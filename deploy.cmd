@echo off

echo ---Deployment site


copy d:\home\site\repository\layoutdb\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war