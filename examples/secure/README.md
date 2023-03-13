# Run the exmaple

1. Run XAMPP

2. `make secure`

3. `make securerun`

On Windows:

`java -cp ".;<Full path to connector>" examples.secure.DBInit "com.mysql.jdbc.Driver" "jdbc:mysql://localhost/secure" "root" "."`

And you need to hardcode the empty password when initalising the connection because for some reason you can't pass empt string as argument.

## Fixing the code

Look for three security flaws in the code and fix them.

<details>
    <summary>Clue 1.</summary>

Concatonating strings entered by the user without checking is a security flaw.

</details>

<details>
    <summary>
        Clue 2.
    </summary>

Add text

</details>

<details>
    <summary>
        Clue 3.
    </summary>

Add text

</details>