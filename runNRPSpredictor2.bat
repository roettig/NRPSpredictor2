@echo off
java -Ddatadir=data -cp build\NRPSpredictor2.jar;lib\Utilities.jar;lib\dom4j-1.6.1.jar;lib\java-getopt-1.0.13.jar;lib\jaxen-1.1-beta-6.jar;lib\libsvm.jar org.roettig.NRPSpredictor2.NRPSpredictor2 %*
