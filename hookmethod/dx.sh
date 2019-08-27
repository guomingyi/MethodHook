#!/bin/bash

#in=./ant-lib/out/pkg.jar
in=build/outputs/jar/hookmethod.jar
out=build/outputs/jar/hookmethod_dex.jar

dx=/home/android/Android/sdk/build-tools/29.0.0/dx

echo "$dx --dex --output=$out $in"
$dx --dex --output=$out $in

ls $PWD/$out -alh


