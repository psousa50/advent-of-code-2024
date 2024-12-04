#!/bin/sh

# get day number argument
DAY_ARG=$1
PART_ARG=$2
SAMPLE_ARG=$3

if [ -n "$DAY_ARG" ]; then
  DAY="--day=$DAY_ARG"
fi

if [ -n "$PART_ARG" ]; then
  PART="--part=$PART_ARG"
fi

if [ -n "$SAMPLE_ARG" ]; then
  SAMPLE="--sample=$SAMPLE_ARG"

fi

./gradlew run --args="$DAY $PART $SAMPLE"
