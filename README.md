![Blueberry Banner](https://raw.github.com/narrowtux/Blueberry/master/src/main/resources/static/img/blueberry-banner.png)
# What is Blueberry?
Blueberry is a lightweight API for creating webclients for your java service.

Copyright &copy; 2012, narrowtux

# License
Blueberry is licensed under the [GNU Lesser General Public License Version 3](http://www.gnu.org/licenses/lgpl.html)

# Compiling
Blueberry uses Maven to handle its dependencies.

To compile, run `mvn clean install`.

# Coding and Pull Request Formatting
* Generally follow the Oracle coding standards.
* Use tabs, no spaces.
* No trailing whitespaces.
* 200 column limit for readability.
* All changes made via pull requests first be compiled locally to verify that the code does indeed compile, and tested to verify that it actually works.
* Where practical, a test should be included to verify the change. Except in exceptional cases, bug fixes **MUST** include a test case which fails for the current version and passes for the updated version.
* Commit messages must include:
    - A brief description of the change
    - A more detailed description of the change (second line and below, optional)
    - Sign-off, verifying agreement with the license terms
* Number of commits in a pull request should be kept to **one commit** and all additional commits must be **squashed** except for circumstantial exceptions.
* You may have more than one commit in a pull request if the commits are separate changes, otherwise squash the commits.
* For clarification, [see the full pull request guidelines](http://wiki.spout.org/display/bridge/Pull+Request+Guidelines).