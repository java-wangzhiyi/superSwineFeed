#A免密登录B
#在A服务器上执行
echo "y\n" | ssh-keygen -t rsa -q -P "" -f ~/.ssh/id_rsa
#-t 秘钥类型
#-q 安静输出
#-P 提供密语
#-f 生成路径
cat ~/.ssh/id_rsa.pub

#在B服务器执行,将上一步获取的字符串粘贴进如下文件，注意一个公钥是一行，请勿换行。
vim ~/.ssh/authorized_keys
