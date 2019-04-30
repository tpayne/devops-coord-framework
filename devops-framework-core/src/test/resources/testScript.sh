#!/bin/sh
#
command=`basename $0`
direct=`dirname $0` 
trap 'stty echo; echo "${command} aborted"; exit_error; exit' 1 2 3 15
#
CWD=`pwd`

#General
tmpdir="/tmp/"
logfile=
sys_passwd=
verbose=0
opmode=0

#
# Echo messages
#
echo_mess()
{
if [ "x${logfile}" = "x" ]; then
   echo "${command}:" $*
else
   echo "${command}:" $* >> ${logfile} 2>&1
fi
}

debug_mess()
{
if [ ${verbose} -eq 0 ]; then
   :
else
   echo_mess $*
fi
return
}

catdebug()
{
if [ ${verbose} -eq 0 ]; then
   :
else
    if [ "x${logfile}" = "x" ]; then
       cat $*
    else
       cat $* >> ${logfile} 2>&1
    fi
fi
return
}

#
# Exit error
#
exit_error()
{
exit 1
}

#
# Usage
#
usage()
{
#
while [ $# -ne 0 ] ; do
        case $1 in
             -l) logfile=$2
                 shift 2;;
             -mode) if [ "x$2" = "xerror" ]; then
                       return 1
                    fi
                    shift 2;;
             --debug) set -xv ; shift;;
             -v) verbose=1
                 shift;;
             --) shift ; break;;
             -|*) break;;
        esac
done

return 0
}

echo_mess "This is a test script"
usage $*
if [ $? -gt 0 ]; then
    exit_error 
fi

exit 0

