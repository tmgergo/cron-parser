## How to run
The app is packaged as a jar file, it can be found in the [./test_config](./test_config) folder, and can be run with:

`java -jar cron-parser-1.0.jar`

To test it with an example config, run (from the project's root folder):

`cat ./test_config/example_config.txt | java -jar ./test_config/cron-parser-1.0.jar 16:10`

### Build
If needed, the project can be re-built by running the `jar` gradle task. From the project's root folder:

`./gradlew jar`

The new jar file will be put in `./build/libs/`

## Test coverage
A detailed test coverage report can be found [here](./coverage-report/ns-1/index.html).

## Assumptions
During development, I made the following assumptions:
- all times in the task config are in 24h format
- the separator between the minute, the hour and the command can be one or multiple whitespaces (e.g. `30 1 /bin/run_me_daily` and `30    1     /bin/run_me_daily` are both valid)
- the task config lines may contain leading and trailing whitespaces (e.g. `30 1 /bin/run_me_daily` and `     30 1 /bin/run_me_daily    ` are both valid) 
- each word after the minute and hour is part of the command (e.g. in case of `30 1 /bin/run_me_daily -A param1` the command is `/bin/run_me_daily -A param1`)
- commands have reasonable length and word count
- the simulated current time has leading 0s (e.g. `03:27` and not `3:27`)
- in the next run output only minutes are padded with zeros (e.g. `3:07` and not `03:07`)

## Comments, considerations, potential future improvements
- One could easily argue that the solution is over-engineered and I wouldn't argue against it. I assumed this would be a solution used long term and so aimed to make it a well architected application.
- Some utility type functionality (e.g. parsing) could be implemented as extension functions. The current utility classes provide better encapsulation, especially around the magic values in `TaskUtils`. The encapsulation around `TaskUtils.ALL_VALUES` could be better, but it is "good enough" for a system with this scope.
- I could have used e.g. `LocalTime` instead of the custom `SimulatedTime` data class, but a more narrow API is probably better here. Also, the name of the class might be too specific, but in this well-defined scope the current name described exactly what this class represents.
- For the error case I could have added better, more descriptive messages

# Requirements

We have a set of tasks, each running at least daily, which are scheduled with a simplified cron. We want to find when each of them will next run.

The scheduler config looks like this:
```
30 1 /bin/run_me_daily
45 * /bin/run_me_hourly
* * /bin/run_me_every_minute
* 19 /bin/run_me_sixty_times
```

The first field is the minutes past the hour, the second field is the hour of the day and the third is the command to run.

For both cases * means that it should run for all values of that field.

In the above example `run_me_daily` has been set to run at 1:30am every day and `run_me_hourly` at 45 minutes past the hour every hour.

The fields are whitespace separated and each entry is on a separate line.

We want you to write a command line program that when fed this config to `stdin` and the simulated 'current time' in the format `HH:MM` as command line argument outputs the soonest time at which each of the commands will fire and whether it is today or tomorrow.

When the task should fire at the simulated 'current time' then that is the time you should output, not the next one.

For example given the above examples as input and the simulated 'current time' command-line argument 16:10 the output should be:
```
1:30 tomorrow - /bin/run_me_daily
16:45 today - /bin/run_me_hourly
16:10 today - /bin/run_me_every_minute
19:00 today - /bin/run_me_sixty_times
```
