#!/usr/bin/expect -f
set r_host host
set r_pwd  passwd

spawn ssh -l work -p 1422 "$r_host"
set timeout 30
expect "work@host's password: "
send "$r_pwd\n"
interact
