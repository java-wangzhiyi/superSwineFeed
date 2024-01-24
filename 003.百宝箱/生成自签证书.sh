#!/bin/bash

# 生成证书的路径
mkdir -p /etc/ssl/certs/licai.bigdata/ && cd /etc/ssl/certs/licai.bigdata/

openssl genrsa -des3 -passout pass:1234 -out licai.bigdata.key 2048
openssl rsa -in licai.bigdata.key -passin pass:1234 -out licai.bigdata.key
openssl req -new -key licai.bigdata.key -out licai.bigdata.csr -subj "/C=CN/ST=Zhejiang/L=Hangzhou/O=licai.bigdata/OU=licai.bigdata/CN=*.licai.bigdata"
openssl x509 -req -days 3650 -in licai.bigdata.csr -signkey licai.bigdata.key -out licai.bigdata.crt
