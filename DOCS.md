# Documentation

This is the documentation of POL.

## Politeness rules

The key aspect of POL is politeness. But what is politeness for POL?

You are polite if your command begins with *please*, such as: ```please calc 12 + 8```

In POL, you mustn't be too polite and too impolite. There are simply two rules to follow:

1. Don't write more than two lines in row which begin with *please*

2. Don't write more than two lines in row which don't begin with *please*

**CAUTION:** All commands in the documentation are written **without** *please* (also if you need *please*).

## Calculate

If you want to add, subtract, multiply, divide or use modulo, you must run the following line:

```calc x + y```
calc x + y
```

Replace *x* and *y* with the values you want to calculate and *+* with the operator (logical operators like ```!=``` are also possible) you want. If you use a logical operator, the result will be *true* or *false*.

## Print

If you want to print a text, simply type:

``` ```
print text
```

Replace *text* with the text you want to print. All backslashes you write will be deleted.

## Prompt

If you want to ask the user for something, run the following line:

``` ```
prompt target prompt_question
```

Replace *prompt_question* with the question you want to ask for. The input will be saved in the variable *target*.

## Set/Get variables

You can get a variable with the following syntax:

``` ```
{variable}
```

There is no special method to set a variable, but you can set the output of a method to a variable:

``` ```
print value > variable
```

Replace *value* with the value you want to set the variable.

Replace *variable* with the variable name you want to get or set.

**CAUTION:** You **can't** set the output of the method ```prompt``` to a variable because the method does it automatically.

## Run a file

See README.md to learn how to run a file by calling the interpreter JAR directly. If you want to run the file in the interpreter, run the following code:

``` ```
run main.pol
```

Replace *main.pol* with the POL file you want to run. The interpreter doesn't check the file extension, but it's a good practice to save POL files with the file extension *.pol*.

## Get a random number

If you want a random number, you need to run one of the following lines:

Run this line, if you want a decimal number:

```
rand 10
```

Run this line, if you want an integer:

```
randi 10
```

Replace *10* with the exclusive maximum of the random number. The minimum is 0.

## Exit the interpreter

Exit the interpreter with the following command:

```
please exit
```

CAUTION: This is a politeness exception. If you want to run `exit`, you **always** need a *please* at the beginning of the line.

## Troubleshooting

If you receive three errors totally in the session, a fatal error will be thrown and the interpreter console exits with status code 20.

If you receive an internal error or a Java exception, please open an issue in the POL repository and answer the following questions, so I can solve the problem:

1. How can I reproduce the error?

2. What was your expected result?

3. What was the code you ran?

4. What version of the interpreter did you use?

5. What version of Java did you use?


