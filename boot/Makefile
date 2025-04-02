#!make
include .env

env:
	sed "s/=.*/=/" .env > .env.example
format:
	mvn spotless::apply