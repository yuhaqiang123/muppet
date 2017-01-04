#!/usr/bin/expect -f
set r_host user@ip
set r_pwd  passwd

spawn ssh -p 1422 "$r_host"
set timeout 30
expect "user@ip's password: "
send "$r_pwd\n"
interact
