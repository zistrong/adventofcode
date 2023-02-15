package com.zistrong.adventofcode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Stack;

public class Day7 {


    /**
     * Now, you're ready to choose a directory to delete.
     * <p>
     * The total disk space available to the filesystem is 70000000. To run the update, you need unused space of at least 30000000.
     * You need to find a directory you can delete that will free up enough space to run the update.
     * <p>
     * In the example above, the total size of the outermost directory (and thus the total amount of used space) is 48381165;
     * this means that the size of the unused space must currently be 21618835, which isn't quite the 30000000 required by the update.
     * Therefore, the update still requires a directory with total size of at least 8381165 to be deleted before it can run.
     * <p>
     * To achieve this, you have the following options:
     * <p>
     * Delete directory e, which would increase unused space by 584.
     * Delete directory a, which would increase unused space by 94853.
     * Delete directory d, which would increase unused space by 24933642.
     * Delete directory /, which would increase unused space by 48381165.
     * <p>
     * Directories e and a are both too small; deleting them would not free up enough space. However, directories d and / are both big enough!
     * Between these, choose the smallest: d, increasing unused space by 24933642.
     * <p>
     * Find the smallest directory that, if deleted, would free up enough space on the filesystem to run the update. What is the total size of that directory?
     */
    @Test
    public void part2() {

        int systemTotal = 70000000;
        int updateSpace = 30000000;

        for (String s : this.content) {
            this.processCommand(s);
        }
        MyFile root = visitStack.firstElement();
        visitStack.clear();
        int totalUsedSize = root.getSize();

        int atLeastToFreeSize = updateSpace - (systemTotal - totalUsedSize);

        this.size = updateSpace;
        findMyFile(root, atLeastToFreeSize);
        System.out.println(size);
        Assert.assertEquals(6400111, size);


    }

    public void findMyFile(MyFile myFile, int atLeastToFreeSize) {
        if (myFile.isDirectory() && myFile.getSize() > atLeastToFreeSize) {
            if (size > myFile.getSize()) {
                size = myFile.getSize();
            }
        }
        for (MyFile child : myFile.getChildren()) {
            findMyFile(child, atLeastToFreeSize);
        }
    }

    int size = 0;

    /**
     * You can hear birds chirping and raindrops hitting leaves as the expedition proceeds.
     * Occasionally, you can even hear much louder sounds in the distance; how big do the animals get out here, anyway?
     * <p>
     * The device the Elves gave you has problems with more than just its communication system. You try to run a system update:
     * <p>
     * $ system-update --please --pretty-please-with-sugar-on-top
     * Error: No space left on device
     * <p>
     * Perhaps you can delete some files to make space for the update?
     * <p>
     * You browse around the filesystem to assess the situation and save the resulting terminal output (your puzzle input). For example:
     * <p>
     * $ cd /
     * $ ls
     * dir a
     * 14848514 b.txt
     * 8504156 c.dat
     * dir d
     * $ cd a
     * $ ls
     * dir e
     * 29116 f
     * 2557 g
     * 62596 h.lst
     * $ cd e
     * $ ls
     * 584 i
     * $ cd ..
     * $ cd ..
     * $ cd d
     * $ ls
     * 4060174 j
     * 8033020 d.log
     * 5626152 d.ext
     * 7214296 k
     * <p>
     * The filesystem consists of a tree of files (plain data) and directories (which can contain other directories or files).
     * The outermost directory is called /. You can navigate around the filesystem,
     * moving into or out of directories and listing the contents of the directory you're currently in.
     * <p>
     * Within the terminal output, lines that begin with $ are commands you executed, very much like some modern computers:
     * <p>
     * cd means change directory. This changes which directory is the current directory, but the specific result depends on the argument:
     * cd x moves in one level: it looks in the current directory for the directory named x and makes it the current directory.
     * cd .. moves out one level: it finds the directory that contains the current directory, then makes that directory the current directory.
     * cd / switches the current directory to the outermost directory, /.
     * ls means list. It prints out all of the files and directories immediately contained by the current directory:
     * 123 abc means that the current directory contains a file named abc with size 123.
     * dir xyz means that the current directory contains a directory named xyz.
     * <p>
     * Given the commands and output in the example above, you can determine that the filesystem looks visually like this:
     * <p>
     * - / (dir)
     * - a (dir)
     * - e (dir)
     * - i (file, size=584)
     * - f (file, size=29116)
     * - g (file, size=2557)
     * - h.lst (file, size=62596)
     * - b.txt (file, size=14848514)
     * - c.dat (file, size=8504156)
     * - d (dir)
     * - j (file, size=4060174)
     * - d.log (file, size=8033020)
     * - d.ext (file, size=5626152)
     * - k (file, size=7214296)
     * <p>
     * Here, there are four directories: / (the outermost directory), a and d (which are in /), and e (which is in a).
     * These directories also contain files of various sizes.
     * <p>
     * Since the disk is full, your first step should probably be to find directories that are good candidates for deletion.
     * To do this, you need to determine the total size of each directory. The total size of a directory is the sum of the
     * sizes of the files it contains, directly or indirectly. (Directories themselves do not count as having any intrinsic size.)
     * <p>
     * The total sizes of the directories above can be found as follows:
     * <p>
     * The total size of directory e is 584 because it contains a single file i of size 584 and no other directories.
     * The directory a has total size 94853 because it contains files f (size 29116), g (size 2557), and h.lst (size 62596),
     * plus file i indirectly (a contains e which contains i).
     * Directory d has total size 24933642.
     * As the outermost directory, / contains every file. Its total size is 48381165, the sum of the size of every file.
     * <p>
     * To begin, find all the directories with a total size of at most 100000, then calculate the sum of their total sizes.
     * In the example above, these directories are a and e; the sum of their total sizes is 95437 (94853 + 584).
     * (As in this example, this process can count files more than once!)
     * <p>
     * Find all the directories with a total size of at most 100000. What is the sum of the total sizes of those directories?
     */
    @Test
    public void part1() {
        for (String s : this.content) {
            this.processCommand(s);
        }
        MyFile root = visitStack.firstElement();
        visitStack.clear();
        getTotalSizeAtMost100000(root);
        System.out.println(this.totalSize);
        Assert.assertEquals(1491614, this.totalSize);
    }

    long totalSize = 0;
    private final Stack<MyFile> visitStack = new Stack<>();
    private MyFile currentDirectory;
    List<String> content;

    private void getTotalSizeAtMost100000(MyFile file) {

        if (file.isDirectory() && file.getSize() <= 100000) {
            totalSize += file.getSize();
        }
        if (file.isDirectory()) {
            for (MyFile child : file.getChildren()) {
                getTotalSizeAtMost100000(child);
            }
        }
    }


    /**
     * stack， save directory， cd xx， push stack， cd .. , pop stack
     */
    @Before
    public void readContent() throws IOException {
        this.content = Files.readAllLines(Path.of("./src/test/resources/", "day7.input"));


    }

    private MyFile findChildFile(String name, MyFile myFile) {

        if (myFile == null) {//root dir
            return new MyFile(name, true);
        }
        return myFile.getChildren().stream().filter(item -> item.getName().equals(name)).findFirst().orElse(new MyFile(name, true));
    }

    private void processCommand(String str) {
        if (str.startsWith("$")) {
            if (str.equals("$ cd ..")) {//return parent dir
                this.visitStack.pop();
                this.currentDirectory = this.visitStack.peek();
            } else if (str.startsWith("$ cd ")) {// child dir
                this.currentDirectory = this.visitStack.push(this.findChildFile(str.split(" ")[2], this.currentDirectory));
            }

        } else if (str.startsWith("dir")) {
            this.currentDirectory.addChild(new MyFile(str.split(" ")[1], true));
        } else {
            this.currentDirectory.addChild(new MyFile(str.split(" ")[1], Integer.parseInt(str.split(" ")[0])));
        }
    }


}

