#!/bin/sh

# get day number argument
DAY=$1

if [ -z "$DAY" ]; then
  echo "Usage: create-day.sh <day-number>"
  exit 1
fi

python ./create-new-day/new-day.py $DAY
