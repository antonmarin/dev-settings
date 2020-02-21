.DEFAULT_GOAL=help

help:
	@printf "make test\t test library\n"

test:
	vendor/bin/codecept run
