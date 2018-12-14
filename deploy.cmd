@echo off

echo ---Deployment site


copy d:\home\site\repository\B00766857exam\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war