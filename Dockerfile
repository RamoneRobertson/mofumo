FROM ubuntu:latest
LABEL authors="ramone"

ENTRYPOINT ["top", "-b"]