# Git-Like Version Control System (Java)

This project is my implementation of a simplified version of **Git** built in Java.  
It is inspired by the ["Build Your Own Git" Challenge](https://codecrafters.io/challenges/git), but all the coding, debugging, and feature implementation here has been done by me.

Git is a version control system used to track changes in source code. In this project, I’ve built a minimal Git clone that can initialize repositories, create commits, and even clone a public GitHub repository.

---

## 🚀 Features Implemented

- **Initialize a repository** (`.git` directory structure)
- **Read blob objects** (understand how Git stores file content)
- **Create blob objects** (write your own file snapshots)
- **Read tree objects** (track directory contents)
- **Write tree objects** (store snapshots of directory structure)
- **Create commits** (link blobs and trees into a commit history)
- **Clone a repository** (fetch data from a remote GitHub repository)

---

## 🛠️ Requirements

- **Java 17+** (LTS recommended)
- **Maven** (to manage dependencies and build the project)

---

## ▶️ How to Run

1. Clone this repository:
   ```bash
   git clone git@github.com:tulsyanlakshya009/git-like-version-control-system.git
   cd git-like-version-control-system
   ```

2. Compile and run using the provided script:

   ```bash
   ./your_program.sh
   ```

3. Example: Initialize a new repo in a safe test directory

   ```bash
   mkdir -p /tmp/testing && cd /tmp/testing
   /path/to/your/repo/your_program.sh init
   ```

   Or, to make it easier, add an alias:

   ```bash
   alias mygit=/path/to/your/repo/your_program.sh
   mkdir -p /tmp/testing && cd /tmp/testing
   mygit init
   ```

---

## 🧪 Testing

To test features without corrupting this repository’s own `.git` folder:

1. Create a new directory:

   ```bash
   mkdir /tmp/git-test && cd /tmp/git-test
   ```

2. Run your Git commands from there:

   ```bash
   /path/to/your/repo/your_program.sh init
   ```

   Example workflow:

   * Initialize: `mygit init`
   * Add blob: `mygit hash-object <file>`
   * Write tree: `mygit write-tree`
   * Commit: `mygit commit`
   * Clone: `mygit clone <repo-url>`

---

## 📚 Learning Outcomes

* Internals of the `.git` directory
* How Git represents data using **objects** (blobs, trees, commits)
* Basics of Git’s **plumbing commands**
* How cloning and commits actually work under the hood

---

## 📝 License

This project is for educational purposes. Feel free to explore and adapt the code.


