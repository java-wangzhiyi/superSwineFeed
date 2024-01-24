#!/bin/bash
find "./" -type f \( -name "*.repositories" -o -name "*.last*" \) -exec rm -f {} \;
