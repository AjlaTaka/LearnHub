package com.softeng.learnhub.utils;

import com.softeng.learnhub.R;
import com.softeng.learnhub.models.Course;
import com.softeng.learnhub.models.Lesson;
import com.softeng.learnhub.models.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DataRepository — single source of truth for all educational content.
 *
 * Architecture role (MVC): this is the Model layer.
 * Activities (Controllers) request data here; they never construct data themselves.
 *
 * All 5 Software Engineering courses are defined here with:
 *   - Course metadata
 *   - 4 lessons per course (with explanations and optional code snippets)
 *   - 10 multiple-choice questions per course with answer explanations
 */
public class DataRepository {

    private static DataRepository instance;
    private List<Course> courses;

    // ── Singleton ─────────────────────────────────────────────────────────────

    private DataRepository() {
        courses = buildAllCourses();
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public List<Course> getAllCourses() {
        return courses;
    }

    public Course getCourseById(int id) {
        for (Course c : courses) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    // ── Course Builder ─────────────────────────────────────────────────────────

    private List<Course> buildAllCourses() {
        List<Course> list = new ArrayList<>();
        list.add(buildDSACourse());
        list.add(buildOSCourse());
        list.add(buildDBCourse());
        list.add(buildOOPCourse());
        list.add(buildNetworksCourse());
        return list;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COURSE 1 — Data Structures & Algorithms
    // ═══════════════════════════════════════════════════════════════════════════

    private Course buildDSACourse() {
        Course c = new Course(
                Course.COURSE_DSA,
                "Data Structures & Algorithms",
                "Arrays, Trees, Graphs & Complexity",
                "Master fundamental data structures and algorithm design techniques essential for every software engineer. Covers Big-O analysis, sorting, trees, graphs, and dynamic programming.",
                "ic_course_dsa",
                R.color.color_dsa,
                4
        );

        // ── Lessons ──────────────────────────────────────────────────────────

        List<Lesson> lessons = new ArrayList<>();

        lessons.add(new Lesson(
                "Big-O Notation & Complexity",
                "Big-O notation describes the upper bound of an algorithm's time or space growth relative to input size n.\n\n" +
                "Common complexities (best → worst):\n" +
                "• O(1) — Constant: array index access\n" +
                "• O(log n) — Logarithmic: binary search\n" +
                "• O(n) — Linear: linear search\n" +
                "• O(n log n) — Merge sort, Heap sort\n" +
                "• O(n²) — Bubble sort, Selection sort\n" +
                "• O(2ⁿ) — Recursive Fibonacci\n\n" +
                "Always analyse both time and space complexity. Prefer O(n log n) sorting over O(n²) for large datasets.\n\n" +
                "Best/Average/Worst cases matter: QuickSort is O(n log n) average but O(n²) worst case.",
                "// O(log n) — Binary Search\nint binarySearch(int[] arr, int target) {\n    int lo = 0, hi = arr.length - 1;\n    while (lo <= hi) {\n        int mid = lo + (hi - lo) / 2;\n        if (arr[mid] == target) return mid;\n        if (arr[mid] < target) lo = mid + 1;\n        else hi = mid - 1;\n    }\n    return -1;\n}"
        ));

        lessons.add(new Lesson(
                "Arrays, Stacks & Queues",
                "Arrays are contiguous memory blocks with O(1) random access. Dynamic arrays (ArrayList in Java) resize automatically.\n\n" +
                "Stack — LIFO (Last-In, First-Out):\n" +
                "• push(x), pop(), peek() — all O(1)\n" +
                "• Applications: function call stack, undo/redo, expression parsing\n\n" +
                "Queue — FIFO (First-In, First-Out):\n" +
                "• enqueue(x), dequeue() — all O(1) with a linked list\n" +
                "• Applications: BFS traversal, task scheduling, print queues\n\n" +
                "Deque (Double-Ended Queue) supports insertion and removal at both ends.",
                "// Stack using Deque (preferred over Stack class)\nDeque<Integer> stack = new ArrayDeque<>();\nstack.push(10);   // push\nint top = stack.peek(); // peek without removing\nstack.pop();      // pop\n\n// Queue using LinkedList\nQueue<Integer> queue = new LinkedList<>();\nqueue.offer(5);   // enqueue\nqueue.poll();     // dequeue"
        ));

        lessons.add(new Lesson(
                "Trees & Binary Search Trees",
                "A tree is a hierarchical data structure with a root node and subtrees of children.\n\n" +
                "Binary Tree: each node has at most 2 children (left, right).\n\n" +
                "Binary Search Tree (BST): left child < parent < right child.\n" +
                "• Search: O(h) where h = height. Balanced BST: O(log n).\n" +
                "• Insertion: O(h). Deletion: O(h).\n\n" +
                "Traversal orders:\n" +
                "• In-order (L→Root→R): produces sorted output for BST\n" +
                "• Pre-order (Root→L→R): used for copying a tree\n" +
                "• Post-order (L→R→Root): used for deleting a tree\n\n" +
                "Balanced BSTs (AVL, Red-Black) guarantee O(log n) operations by maintaining height balance.",
                "// In-order traversal (recursive)\nvoid inOrder(TreeNode node) {\n    if (node == null) return;\n    inOrder(node.left);\n    System.out.print(node.val + \" \");\n    inOrder(node.right);\n}\n\n// BST insertion\nTreeNode insert(TreeNode root, int val) {\n    if (root == null) return new TreeNode(val);\n    if (val < root.val) root.left = insert(root.left, val);\n    else root.right = insert(root.right, val);\n    return root;\n}"
        ));

        lessons.add(new Lesson(
                "Graphs & Dynamic Programming",
                "Graph G = (V, E): a set of vertices V connected by edges E.\n" +
                "• Directed vs Undirected\n" +
                "• Weighted vs Unweighted\n" +
                "• Representations: Adjacency Matrix O(V²), Adjacency List O(V+E)\n\n" +
                "BFS (Breadth-First Search): explores level by level. Uses a Queue.\n" +
                "DFS (Depth-First Search): explores as deep as possible. Uses Stack/Recursion.\n\n" +
                "Shortest paths:\n" +
                "• Dijkstra: non-negative weights, O((V+E) log V)\n" +
                "• Bellman-Ford: handles negative weights, O(VE)\n\n" +
                "Dynamic Programming: solve complex problems by breaking into overlapping subproblems.\n" +
                "• Memoization (top-down) vs Tabulation (bottom-up)\n" +
                "• Classic problems: Fibonacci, Knapsack, Longest Common Subsequence",
                "// Fibonacci with memoization\nint[] memo = new int[100];\nint fib(int n) {\n    if (n <= 1) return n;\n    if (memo[n] != 0) return memo[n];\n    return memo[n] = fib(n-1) + fib(n-2);\n}"
        ));

        c.setLessons(lessons);

        // ── Quiz Questions ────────────────────────────────────────────────────

        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What is the time complexity of searching in a balanced Binary Search Tree?",
                new String[]{"O(1)", "O(log n)", "O(n)", "O(n²)"},
                1,
                "A balanced BST has height O(log n). Each comparison eliminates half the remaining nodes, similar to binary search on a sorted array."
        ));
        questions.add(new Question(
                "Which data structure uses LIFO (Last-In, First-Out) ordering?",
                new String[]{"Queue", "Heap", "Stack", "Graph"},
                2,
                "A Stack processes the most recently added element first (LIFO). It's used for function call management, undo operations, and expression parsing."
        ));
        questions.add(new Question(
                "What does Big-O notation describe?",
                new String[]{"The exact runtime of an algorithm", "The best-case performance", "The upper bound of growth rate", "Memory usage only"},
                2,
                "Big-O describes the worst-case upper bound — how an algorithm's resource usage grows relative to input size n."
        ));
        questions.add(new Question(
                "Which sorting algorithm has the best average-case time complexity?",
                new String[]{"Bubble Sort — O(n²)", "Insertion Sort — O(n²)", "Merge Sort — O(n log n)", "Selection Sort — O(n²)"},
                2,
                "Merge Sort consistently achieves O(n log n) in all cases (best, average, worst) by dividing the array and merging sorted halves."
        ));
        questions.add(new Question(
                "In a graph, what does BFS use to track nodes to visit?",
                new String[]{"Stack", "Queue", "Priority Queue", "Array"},
                1,
                "BFS (Breadth-First Search) uses a Queue to explore nodes level by level, ensuring the shortest path in an unweighted graph."
        ));
        questions.add(new Question(
                "Which traversal of a BST produces nodes in sorted order?",
                new String[]{"Pre-order", "Post-order", "Level-order", "In-order"},
                3,
                "In-order traversal (Left → Root → Right) visits BST nodes in ascending sorted order by design of the BST property."
        ));
        questions.add(new Question(
                "Dynamic Programming is best applied when a problem has:",
                new String[]{"No subproblems", "Independent subproblems only", "Overlapping subproblems and optimal substructure", "Only greedy choices"},
                2,
                "DP works when subproblems overlap (same subproblem solved multiple times) and exhibit optimal substructure (optimal solution built from optimal sub-solutions)."
        ));
        questions.add(new Question(
                "What is the space complexity of an adjacency matrix for a graph with V vertices?",
                new String[]{"O(V)", "O(E)", "O(V + E)", "O(V²)"},
                3,
                "An adjacency matrix stores a V×V grid of edges, requiring O(V²) space regardless of the number of edges."
        ));
        questions.add(new Question(
                "Which algorithm finds the shortest path in a weighted graph with non-negative edges?",
                new String[]{"DFS", "BFS", "Dijkstra", "Bellman-Ford"},
                2,
                "Dijkstra's algorithm uses a priority queue to greedily select the next closest vertex, working correctly for non-negative edge weights."
        ));
        questions.add(new Question(
                "What is the worst-case time complexity of QuickSort?",
                new String[]{"O(n log n)", "O(n)", "O(n²)", "O(log n)"},
                2,
                "QuickSort degrades to O(n²) when the pivot is consistently the smallest or largest element (e.g., already-sorted input with a bad pivot choice)."
        ));

        c.setQuestions(questions);
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COURSE 2 — Operating Systems
    // ═══════════════════════════════════════════════════════════════════════════

    private Course buildOSCourse() {
        Course c = new Course(
                Course.COURSE_OS,
                "Operating Systems",
                "Processes, Memory & File Systems",
                "Understand how modern operating systems manage processes, memory, concurrency, and I/O. Essential knowledge for systems programming and software architecture.",
                "ic_course_os",
                R.color.color_os,
                4
        );

        List<Lesson> lessons = new ArrayList<>();

        lessons.add(new Lesson(
                "Processes & Threads",
                "A process is an instance of a running program with its own memory space (code, data, heap, stack).\n\n" +
                "Process States: New → Ready → Running → Waiting → Terminated\n\n" +
                "A thread is the smallest unit of CPU execution within a process.\n" +
                "• Threads within the same process share heap and global memory\n" +
                "• Each thread has its own stack and registers\n\n" +
                "Multithreading benefits:\n" +
                "• Improved responsiveness (UI thread + worker threads)\n" +
                "• Better CPU utilization on multicore systems\n\n" +
                "Context switching: the OS saves the current process state (PCB — Process Control Block) and loads another.",
                null
        ));

        lessons.add(new Lesson(
                "CPU Scheduling Algorithms",
                "The CPU scheduler decides which ready process runs next.\n\n" +
                "Key metrics:\n" +
                "• CPU Utilization: keep CPU as busy as possible\n" +
                "• Throughput: processes completed per time unit\n" +
                "• Turnaround Time: submission to completion\n" +
                "• Waiting Time: time spent in ready queue\n" +
                "• Response Time: first response after submission\n\n" +
                "Algorithms:\n" +
                "• FCFS (First-Come-First-Served): simple, convoy effect problem\n" +
                "• SJF (Shortest Job First): optimal average waiting time, needs burst prediction\n" +
                "• Round Robin: preemptive, time quantum q; good for time-sharing\n" +
                "• Priority Scheduling: starvation risk → aging solution\n" +
                "• Multilevel Queue: separate queues for different process types",
                null
        ));

        lessons.add(new Lesson(
                "Memory Management & Virtual Memory",
                "Physical memory (RAM) is managed by the OS through several techniques:\n\n" +
                "Contiguous Allocation: each process occupies a single contiguous block.\n" +
                "• Fixed partitioning: internal fragmentation\n" +
                "• Dynamic partitioning: external fragmentation\n\n" +
                "Paging: divide memory into fixed-size frames; process into equal-size pages.\n" +
                "• Eliminates external fragmentation\n" +
                "• Page table maps logical → physical addresses\n\n" +
                "Virtual Memory: gives each process an illusion of large, private memory.\n" +
                "• Demand paging: load pages only when needed\n" +
                "• Page fault: page not in RAM → OS loads from disk\n" +
                "• Page replacement algorithms: FIFO, LRU, Optimal\n\n" +
                "Thrashing: excessive page faults causing more paging than execution.",
                null
        ));

        lessons.add(new Lesson(
                "Deadlock & Synchronization",
                "Deadlock: a set of processes blocked forever, each waiting for a resource held by another.\n\n" +
                "Four Coffman conditions (ALL must hold):\n" +
                "1. Mutual Exclusion — resource non-shareable\n" +
                "2. Hold and Wait — process holds one, waits for another\n" +
                "3. No Preemption — resources released only voluntarily\n" +
                "4. Circular Wait — circular chain of waiting processes\n\n" +
                "Handling deadlock:\n" +
                "• Prevention: eliminate one condition\n" +
                "• Avoidance: Banker's Algorithm\n" +
                "• Detection & Recovery: allow deadlock, then fix\n\n" +
                "Synchronization primitives:\n" +
                "• Mutex: binary lock — only lock owner can unlock\n" +
                "• Semaphore: integer counter — can be used by multiple threads\n" +
                "• Monitor: high-level construct with condition variables",
                "// Java synchronized method (mutex equivalent)\nsynchronized void deposit(int amount) {\n    balance += amount;\n}\n\n// Java Semaphore\nSemaphore sem = new Semaphore(3); // 3 permits\nsem.acquire(); // block if 0 permits\n// ... critical section ...\nsem.release();"
        ));

        c.setLessons(lessons);

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
                "Which of the following is NOT one of the four Coffman deadlock conditions?",
                new String[]{"Mutual Exclusion", "Hold and Wait", "Preemption Allowed", "Circular Wait"},
                2,
                "'No Preemption' is the actual condition — resources cannot be forcibly taken. 'Preemption Allowed' would prevent deadlock, not cause it."
        ));
        questions.add(new Question(
                "What scheduling algorithm gives the minimum average waiting time?",
                new String[]{"FCFS", "Round Robin", "SJF (Shortest Job First)", "Priority Scheduling"},
                2,
                "SJF minimizes average waiting time by always scheduling the shortest available job next. It is theoretically optimal but requires knowing burst times in advance."
        ));
        questions.add(new Question(
                "What is a page fault?",
                new String[]{"A hardware memory error", "Accessing a page not currently in RAM", "A CPU arithmetic error", "A file system corruption"},
                1,
                "A page fault occurs when a process accesses a virtual page that is not loaded in physical RAM. The OS must load it from disk (swap space)."
        ));
        questions.add(new Question(
                "What does a Process Control Block (PCB) store?",
                new String[]{"File system data", "Process state, registers, and scheduling info", "Physical memory addresses only", "Network socket information"},
                1,
                "The PCB is the OS data structure for a process. It stores state, program counter, CPU registers, memory limits, and scheduling data."
        ));
        questions.add(new Question(
                "Thrashing in virtual memory occurs when:",
                new String[]{"CPU utilization is very high", "Processes spend more time paging than executing", "All processes are terminated", "RAM is fully empty"},
                1,
                "Thrashing happens when the working sets of processes together exceed available RAM, causing constant page faults and disk I/O that dominates CPU time."
        ));
        questions.add(new Question(
                "Which synchronization primitive allows multiple threads to access a resource up to a limit?",
                new String[]{"Mutex", "Monitor", "Semaphore", "Spinlock"},
                2,
                "A semaphore maintains an integer count of available permits. It's ideal for controlling access to a pool of limited resources (e.g., 3 database connections)."
        ));
        questions.add(new Question(
                "In Round Robin scheduling, what is the 'time quantum'?",
                new String[]{"Total CPU time for all processes", "Maximum time a process runs before being preempted", "Minimum burst time", "Priority level"},
                1,
                "The time quantum (q) is the fixed time slice each process gets. If not finished, it's preempted and placed at the back of the ready queue."
        ));
        questions.add(new Question(
                "Which memory allocation technique eliminates external fragmentation?",
                new String[]{"Contiguous Allocation", "Fixed Partitioning", "Paging", "Variable Partitioning"},
                2,
                "Paging divides memory into equal-sized frames and processes into equal-sized pages. Since all pages are the same size, external fragmentation cannot occur."
        ));
        questions.add(new Question(
                "What is the main difference between a process and a thread?",
                new String[]{"Threads have separate memory spaces", "Processes share the same memory space", "Threads are heavier than processes", "Threads within a process share memory; processes have separate spaces"},
                3,
                "Each process has its own isolated virtual address space. Threads within the same process share heap and global memory, making them lighter and faster to create."
        ));
        questions.add(new Question(
                "The Banker's Algorithm is used for:",
                new String[]{"Memory paging", "Deadlock avoidance", "CPU scheduling", "File allocation"},
                1,
                "The Banker's Algorithm simulates resource allocation to determine if granting a request leaves the system in a safe state, thereby avoiding deadlock."
        ));

        c.setQuestions(questions);
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COURSE 3 — Database Systems
    // ═══════════════════════════════════════════════════════════════════════════

    private Course buildDBCourse() {
        Course c = new Course(
                Course.COURSE_DB,
                "Database Systems",
                "SQL, Normalization & Transactions",
                "Learn relational database design, SQL querying, normalization theory, indexing strategies, and ACID transaction properties used in real-world applications.",
                "ic_course_db",
                R.color.color_db,
                4
        );

        List<Lesson> lessons = new ArrayList<>();

        lessons.add(new Lesson(
                "Relational Model & SQL Basics",
                "The relational model organizes data into tables (relations) of rows (tuples) and columns (attributes).\n\n" +
                "Key concepts:\n" +
                "• Primary Key: uniquely identifies each row\n" +
                "• Foreign Key: references a primary key in another table\n" +
                "• Schema: structure definition of a table\n\n" +
                "Core SQL operations:\n" +
                "• SELECT — retrieve data\n" +
                "• INSERT — add rows\n" +
                "• UPDATE — modify rows\n" +
                "• DELETE — remove rows\n" +
                "• JOIN — combine tables (INNER, LEFT, RIGHT, FULL)\n\n" +
                "WHERE filters rows; GROUP BY aggregates; HAVING filters groups; ORDER BY sorts results.",
                "-- Find students with GPA > 3.5\nSELECT s.name, s.gpa\nFROM students s\nINNER JOIN enrollments e ON s.id = e.student_id\nWHERE s.gpa > 3.5\nGROUP BY s.id\nHAVING COUNT(e.course_id) >= 3\nORDER BY s.gpa DESC;"
        ));

        lessons.add(new Lesson(
                "Normalization (1NF–3NF & BCNF)",
                "Normalization eliminates redundancy and anomalies through decomposition.\n\n" +
                "1NF (First Normal Form):\n" +
                "• All attributes are atomic (no repeating groups or arrays)\n" +
                "• Each row is unique\n\n" +
                "2NF (Second Normal Form):\n" +
                "• Is in 1NF\n" +
                "• No partial dependency (non-key attribute depends on part of a composite key)\n\n" +
                "3NF (Third Normal Form):\n" +
                "• Is in 2NF\n" +
                "• No transitive dependency (non-key attribute depends on another non-key attribute)\n\n" +
                "BCNF (Boyce-Codd NF):\n" +
                "• For every non-trivial FD X→Y, X must be a superkey\n" +
                "• Stricter than 3NF\n\n" +
                "Anomalies fixed by normalization: insertion anomaly, deletion anomaly, update anomaly.",
                null
        ));

        lessons.add(new Lesson(
                "Indexing & Query Optimization",
                "Indexes speed up data retrieval at the cost of extra storage and slower writes.\n\n" +
                "B-Tree Index (most common):\n" +
                "• Balanced tree structure\n" +
                "• O(log n) search, insert, delete\n" +
                "• Supports range queries (BETWEEN, >, <)\n\n" +
                "Hash Index:\n" +
                "• O(1) exact match lookups\n" +
                "• Does NOT support range queries\n\n" +
                "Clustered Index: rows stored in index order (one per table, usually primary key)\n" +
                "Non-clustered Index: separate structure pointing to rows\n\n" +
                "Query Optimization:\n" +
                "• EXPLAIN/EXPLAIN ANALYZE: shows query execution plan\n" +
                "• Select only needed columns (avoid SELECT *)\n" +
                "• Filter early with WHERE before JOIN\n" +
                "• Use covering indexes for frequently-queried columns",
                "-- Create a composite index for common query pattern\nCREATE INDEX idx_student_course\nON enrollments (student_id, course_id);\n\n-- EXPLAIN to inspect the plan\nEXPLAIN SELECT * FROM students WHERE gpa > 3.5;"
        ));

        lessons.add(new Lesson(
                "Transactions & ACID Properties",
                "A transaction is a sequence of database operations treated as a single logical unit.\n\n" +
                "ACID Properties:\n" +
                "• Atomicity: all operations succeed, or all are rolled back\n" +
                "• Consistency: transaction brings DB from one valid state to another\n" +
                "• Isolation: concurrent transactions appear sequential\n" +
                "• Durability: committed transactions survive crashes (written to disk)\n\n" +
                "Isolation Levels (weakest → strongest):\n" +
                "1. Read Uncommitted — dirty reads possible\n" +
                "2. Read Committed — no dirty reads; non-repeatable reads possible\n" +
                "3. Repeatable Read — no non-repeatable reads; phantom reads possible\n" +
                "4. Serializable — full isolation; lowest concurrency\n\n" +
                "Concurrency problems:\n" +
                "• Dirty Read: reading uncommitted data\n" +
                "• Non-repeatable Read: same row changed between two reads\n" +
                "• Phantom Read: new rows appear between two range queries",
                "-- Transaction example (bank transfer)\nBEGIN TRANSACTION;\n  UPDATE accounts SET balance = balance - 500\n  WHERE account_id = 1;\n  \n  UPDATE accounts SET balance = balance + 500\n  WHERE account_id = 2;\nCOMMIT; -- or ROLLBACK on error"
        ));

        c.setLessons(lessons);

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
                "Which SQL clause is used to filter results of an aggregate function?",
                new String[]{"WHERE", "HAVING", "GROUP BY", "ORDER BY"},
                1,
                "HAVING filters groups created by GROUP BY, whereas WHERE filters individual rows before grouping. For example: HAVING COUNT(*) > 5."
        ));
        questions.add(new Question(
                "What does ACID stand for in database transactions?",
                new String[]{"Atomicity, Consistency, Isolation, Durability", "Access, Control, Index, Data", "Atomic, Committed, Isolated, Durable", "Accuracy, Consistency, Integrity, Dependency"},
                0,
                "ACID = Atomicity (all-or-nothing), Consistency (valid state transitions), Isolation (concurrent transactions appear serial), Durability (commits persist through crashes)."
        ));
        questions.add(new Question(
                "A table is in 3NF if it is in 2NF and:",
                new String[]{"Has no multi-valued attributes", "Has no transitive dependencies", "Every FD has a superkey on the left", "Has no partial dependencies"},
                1,
                "3NF requires: no partial dependencies (2NF) AND no transitive dependencies (non-key attribute depending on another non-key attribute)."
        ));
        questions.add(new Question(
                "Which type of index supports range queries (BETWEEN, >, <)?",
                new String[]{"Hash Index", "Bitmap Index", "B-Tree Index", "Dense Index"},
                2,
                "B-Tree indexes maintain sorted order, enabling efficient range scans. Hash indexes compute a hash for exact lookup only and cannot support ordered range queries."
        ));
        questions.add(new Question(
                "What is a 'dirty read' in database concurrency?",
                new String[]{"Reading corrupted data from disk", "Reading data modified by an uncommitted transaction", "Reading from an empty table", "Reading without an index"},
                1,
                "A dirty read occurs when Transaction A reads data written by Transaction B, which then rolls back. A now has data that never officially existed."
        ));
        questions.add(new Question(
                "Which JOIN type returns all rows from both tables, with NULLs for non-matches?",
                new String[]{"INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN"},
                3,
                "FULL OUTER JOIN returns all rows from both tables. Non-matching rows from either side are filled with NULL values."
        ));
        questions.add(new Question(
                "What is the purpose of a Foreign Key?",
                new String[]{"Uniquely identify each row in a table", "Encrypt sensitive columns", "Reference a Primary Key in another table to enforce referential integrity", "Index a column for fast search"},
                2,
                "A Foreign Key creates a link between tables and enforces referential integrity — you cannot insert a FK value that doesn't exist in the referenced table."
        ));
        questions.add(new Question(
                "Which isolation level prevents dirty reads but allows non-repeatable reads?",
                new String[]{"Read Uncommitted", "Read Committed", "Repeatable Read", "Serializable"},
                1,
                "Read Committed ensures you only see committed data (no dirty reads). However, another transaction can modify and commit between your reads (non-repeatable reads)."
        ));
        questions.add(new Question(
                "What does EXPLAIN do in SQL?",
                new String[]{"Creates a table structure", "Shows the query execution plan", "Exports data to CSV", "Validates syntax only"},
                1,
                "EXPLAIN (or EXPLAIN ANALYZE) shows how the query optimizer plans to execute a query — which indexes are used, join methods, and estimated costs."
        ));
        questions.add(new Question(
                "Normalization primarily aims to:",
                new String[]{"Increase query speed", "Eliminate data redundancy and update anomalies", "Add more indexes", "Encrypt the database"},
                1,
                "Normalization decomposes tables to remove redundancy and prevent insertion, update, and deletion anomalies that arise from storing the same data in multiple places."
        ));

        c.setQuestions(questions);
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COURSE 4 — Object-Oriented Programming
    // ═══════════════════════════════════════════════════════════════════════════

    private Course buildOOPCourse() {
        Course c = new Course(
                Course.COURSE_OOP,
                "Object-Oriented Programming",
                "SOLID Principles & Design Patterns",
                "Deep dive into OOP pillars, SOLID design principles, and the most important Gang of Four design patterns. Build maintainable, extensible software systems.",
                "ic_course_oop",
                R.color.color_oop,
                4
        );

        List<Lesson> lessons = new ArrayList<>();

        lessons.add(new Lesson(
                "Four Pillars of OOP",
                "Object-Oriented Programming organizes code around objects that combine data (fields) and behavior (methods).\n\n" +
                "1. Encapsulation:\n" +
                "• Bundle data + methods that operate on it\n" +
                "• Hide implementation via private fields + public getters/setters\n" +
                "• Reduces coupling and prevents invalid state\n\n" +
                "2. Inheritance:\n" +
                "• Subclass inherits fields and methods from superclass\n" +
                "• Promotes code reuse\n" +
                "• 'IS-A' relationship (Dog IS-A Animal)\n\n" +
                "3. Polymorphism:\n" +
                "• Same interface, different implementations\n" +
                "• Method overriding (runtime polymorphism)\n" +
                "• Method overloading (compile-time polymorphism)\n\n" +
                "4. Abstraction:\n" +
                "• Hide complex implementation, expose simple interface\n" +
                "• Abstract classes and interfaces in Java",
                "// Polymorphism example\nabstract class Shape {\n    abstract double area();\n}\nclass Circle extends Shape {\n    double r;\n    double area() { return Math.PI * r * r; }\n}\nclass Rectangle extends Shape {\n    double w, h;\n    double area() { return w * h; }\n}\n// Same call, different behavior\nShape s = new Circle();\ndouble a = s.area();"
        ));

        lessons.add(new Lesson(
                "SOLID Principles",
                "SOLID is an acronym for 5 principles of clean OOP design:\n\n" +
                "S — Single Responsibility Principle:\n" +
                "A class should have only one reason to change.\n\n" +
                "O — Open/Closed Principle:\n" +
                "Classes should be open for extension but closed for modification.\n\n" +
                "L — Liskov Substitution Principle:\n" +
                "Subtypes must be substitutable for their base types without breaking the program.\n\n" +
                "I — Interface Segregation Principle:\n" +
                "Clients should not be forced to depend on methods they don't use. Prefer many small interfaces over one large one.\n\n" +
                "D — Dependency Inversion Principle:\n" +
                "High-level modules should not depend on low-level modules. Both should depend on abstractions (interfaces).\n\n" +
                "Following SOLID leads to code that is easier to test, extend, and maintain.",
                null
        ));

        lessons.add(new Lesson(
                "Creational Design Patterns",
                "Creational patterns deal with object creation mechanisms.\n\n" +
                "Singleton:\n" +
                "• Ensures only one instance of a class exists\n" +
                "• Provides global access point\n" +
                "• Used for: database connections, loggers, configuration managers\n\n" +
                "Factory Method:\n" +
                "• Define interface for creating objects; let subclasses decide which class to instantiate\n" +
                "• Decouples object creation from usage\n\n" +
                "Abstract Factory:\n" +
                "• Creates families of related objects without specifying concrete classes\n\n" +
                "Builder:\n" +
                "• Construct complex objects step by step\n" +
                "• Separates construction from representation\n" +
                "• Excellent for objects with many optional parameters (avoids telescoping constructors)\n\n" +
                "Prototype:\n" +
                "• Create new objects by copying (cloning) an existing object",
                "// Builder Pattern\nUser user = new User.Builder(\"Alice\")\n    .age(22)\n    .email(\"alice@uni.edu\")\n    .studentId(\"CS2024\")\n    .build();\n\n// Singleton (thread-safe)\npublic class AppConfig {\n    private static AppConfig instance;\n    private AppConfig() {}\n    public static synchronized AppConfig getInstance() {\n        if (instance == null) instance = new AppConfig();\n        return instance;\n    }\n}"
        ));

        lessons.add(new Lesson(
                "Structural & Behavioral Patterns",
                "Structural Patterns — how classes and objects are composed:\n\n" +
                "• Adapter: wraps an incompatible interface so it works with client code\n" +
                "• Decorator: adds behavior to objects dynamically without subclassing\n" +
                "• Facade: provides a simplified interface to a complex subsystem\n" +
                "• Composite: treats individual objects and groups uniformly (tree structure)\n\n" +
                "Behavioral Patterns — how objects communicate:\n\n" +
                "• Observer: one-to-many dependency — when one object changes state, all dependents are notified automatically. (Android's LiveData uses this!)\n" +
                "• Strategy: define a family of algorithms, encapsulate each, make them interchangeable\n" +
                "• Command: encapsulate a request as an object, enabling undo/redo\n" +
                "• Template Method: define skeleton of an algorithm in base class; let subclasses fill in specific steps\n" +
                "• Iterator: sequential access to elements of a collection without exposing internal structure",
                "// Observer Pattern (simplified)\ninterface Observer { void update(String event); }\nclass EventBus {\n    List<Observer> listeners = new ArrayList<>();\n    void subscribe(Observer o) { listeners.add(o); }\n    void publish(String event) {\n        for (Observer o : listeners) o.update(event);\n    }\n}"
        ));

        c.setLessons(lessons);

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
                "Which OOP pillar hides implementation details behind a public interface?",
                new String[]{"Inheritance", "Polymorphism", "Encapsulation", "Abstraction"},
                2,
                "Encapsulation bundles data and methods while hiding the internal implementation. Fields are private; access is controlled via public getters/setters."
        ));
        questions.add(new Question(
                "The Liskov Substitution Principle states that:",
                new String[]{"Classes should have one responsibility", "Subclasses must be substitutable for their superclasses", "Depend on abstractions, not concretions", "Prefer interfaces over large classes"},
                1,
                "LSP (L in SOLID): if S is a subtype of T, objects of type T may be replaced with objects of type S without altering program correctness."
        ));
        questions.add(new Question(
                "Which design pattern ensures only one instance of a class exists?",
                new String[]{"Factory Method", "Builder", "Prototype", "Singleton"},
                3,
                "The Singleton pattern restricts instantiation to one object and provides a global access point. Common uses: configuration, loggers, connection pools."
        ));
        questions.add(new Question(
                "Which pattern adds behavior to an object dynamically without subclassing?",
                new String[]{"Adapter", "Decorator", "Facade", "Composite"},
                1,
                "The Decorator pattern wraps an object with another object that adds new behavior. Java's BufferedReader wrapping FileReader is a classic example."
        ));
        questions.add(new Question(
                "Method overriding in Java is an example of:",
                new String[]{"Compile-time polymorphism", "Encapsulation", "Runtime polymorphism", "Static binding"},
                2,
                "Method overriding is resolved at runtime based on the actual object type (dynamic dispatch). This is runtime (dynamic) polymorphism."
        ));
        questions.add(new Question(
                "The Open/Closed Principle means a class should be:",
                new String[]{"Open for modification, closed for extension", "Open for extension, closed for modification", "Open for both extension and modification", "Closed for both"},
                1,
                "OCP: add new behavior by extending (via new subclasses or implementations), not by modifying existing tested code. This protects against regression."
        ));
        questions.add(new Question(
                "Which pattern converts an incompatible interface into one a client expects?",
                new String[]{"Facade", "Bridge", "Adapter", "Proxy"},
                2,
                "The Adapter wraps an existing class with a new interface. Like a power adapter — same device, different plug format."
        ));
        questions.add(new Question(
                "Android's LiveData is an implementation of which pattern?",
                new String[]{"Command", "Strategy", "Observer", "Template Method"},
                2,
                "LiveData implements the Observer pattern. UI components (observers) automatically receive updates when the LiveData (subject) value changes."
        ));
        questions.add(new Question(
                "The Builder pattern is most useful when:",
                new String[]{"You need exactly one instance", "Objects have many optional parameters", "You need to clone objects", "You need to define algorithm families"},
                1,
                "Builder avoids telescoping constructors. Instead of many constructor variants, you chain setter calls and call build() to get the final object."
        ));
        questions.add(new Question(
                "Which SOLID principle suggests preferring many small interfaces over one large one?",
                new String[]{"Single Responsibility", "Open/Closed", "Liskov Substitution", "Interface Segregation"},
                3,
                "ISP: don't force clients to implement methods they don't use. Split large interfaces into smaller, focused ones so classes only implement what they need."
        ));

        c.setQuestions(questions);
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COURSE 5 — Computer Networks
    // ═══════════════════════════════════════════════════════════════════════════

    private Course buildNetworksCourse() {
        Course c = new Course(
                Course.COURSE_NETWORKS,
                "Computer Networks",
                "TCP/IP, HTTP & Network Security",
                "Understand how the Internet works — from physical bits to application protocols. Covers the OSI model, TCP/IP, HTTP/HTTPS, DNS, and fundamental security concepts.",
                "ic_course_networks",
                R.color.color_networks,
                4
        );

        List<Lesson> lessons = new ArrayList<>();

        lessons.add(new Lesson(
                "OSI Model & TCP/IP Stack",
                "The OSI (Open Systems Interconnection) model divides network communication into 7 layers:\n\n" +
                "7. Application — HTTP, FTP, DNS, SMTP (user-facing protocols)\n" +
                "6. Presentation — encryption, compression, data format\n" +
                "5. Session — connection management, authentication\n" +
                "4. Transport — TCP (reliable), UDP (fast, unreliable); ports\n" +
                "3. Network — IP addressing, routing (routers)\n" +
                "2. Data Link — MAC addresses, frames, error detection (switches)\n" +
                "1. Physical — bits, cables, wireless signals\n\n" +
                "TCP/IP Stack (practical 4-layer model):\n" +
                "• Application (layers 5-7)\n" +
                "• Transport (layer 4)\n" +
                "• Internet/Network (layer 3)\n" +
                "• Network Access (layers 1-2)\n\n" +
                "Data encapsulation: each layer adds its header as data travels down, strips it going up.",
                null
        ));

        lessons.add(new Lesson(
                "TCP vs UDP & Sockets",
                "TCP (Transmission Control Protocol):\n" +
                "• Connection-oriented: 3-way handshake (SYN → SYN-ACK → ACK)\n" +
                "• Reliable: acknowledgements, retransmission on loss\n" +
                "• Ordered: data arrives in sequence\n" +
                "• Flow control: prevents overwhelming receiver\n" +
                "• Congestion control: avoids network saturation\n" +
                "• Use cases: HTTP, email, file transfer\n\n" +
                "UDP (User Datagram Protocol):\n" +
                "• Connectionless: no handshake\n" +
                "• Unreliable: no guarantees on delivery or order\n" +
                "• Low overhead, low latency\n" +
                "• Use cases: video streaming, online gaming, DNS queries, VoIP\n\n" +
                "Sockets: endpoint for communication identified by IP + port.\n" +
                "• Well-known ports: HTTP=80, HTTPS=443, DNS=53, SSH=22",
                "// Java TCP Socket\nSocket socket = new Socket(\"api.example.com\", 80);\nOutputStream out = socket.getOutputStream();\nInputStream in = socket.getInputStream();\n// send/receive data\nsocket.close();"
        ));

        lessons.add(new Lesson(
                "HTTP, HTTPS & REST APIs",
                "HTTP (HyperText Transfer Protocol) — application-layer protocol for the Web.\n\n" +
                "HTTP Methods:\n" +
                "• GET: retrieve resource (safe, idempotent)\n" +
                "• POST: create resource (not idempotent)\n" +
                "• PUT: replace resource (idempotent)\n" +
                "• PATCH: partially update resource\n" +
                "• DELETE: remove resource (idempotent)\n\n" +
                "HTTP Status Codes:\n" +
                "• 2xx: Success (200 OK, 201 Created)\n" +
                "• 3xx: Redirect (301 Moved Permanently)\n" +
                "• 4xx: Client Error (400 Bad Request, 401 Unauthorized, 404 Not Found)\n" +
                "• 5xx: Server Error (500 Internal Server Error)\n\n" +
                "HTTPS = HTTP + TLS/SSL encryption.\n" +
                "• TLS handshake establishes shared secret key\n" +
                "• All data encrypted — protects confidentiality and integrity\n\n" +
                "REST API Principles: stateless, resource-based URLs, standard HTTP methods, JSON responses.",
                "// HTTP GET Request (Java)\nURL url = new URL(\"https://api.example.com/users/1\");\nHttpURLConnection conn = (HttpURLConnection) url.openConnection();\nconn.setRequestMethod(\"GET\");\nint responseCode = conn.getResponseCode(); // 200 = OK"
        ));

        lessons.add(new Lesson(
                "DNS, Routing & Network Security",
                "DNS (Domain Name System):\n" +
                "• Translates domain names → IP addresses\n" +
                "• Hierarchical: Root → TLD (.com, .org) → Authoritative servers\n" +
                "• Caching reduces lookup time (TTL controls cache duration)\n\n" +
                "IP Routing:\n" +
                "• Routers forward packets based on routing tables\n" +
                "• BGP (Border Gateway Protocol): routing between ISPs on the Internet\n" +
                "• Subnetting: divide IP space; CIDR notation (192.168.1.0/24)\n\n" +
                "NAT (Network Address Translation): maps private IPs → one public IP.\n\n" +
                "Network Security:\n" +
                "• Firewall: filters traffic by rules (IP, port, protocol)\n" +
                "• TLS/SSL: encryption + authentication using certificates\n" +
                "• Common attacks: SQL Injection, XSS, Man-in-the-Middle, DDoS\n" +
                "• Defense: input validation, HTTPS everywhere, rate limiting, WAF\n\n" +
                "VPN: encrypts traffic through a tunnel to a private network.",
                null
        ));

        c.setLessons(lessons);

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
                "At which OSI layer does IP addressing and routing occur?",
                new String[]{"Layer 2 — Data Link", "Layer 3 — Network", "Layer 4 — Transport", "Layer 7 — Application"},
                1,
                "Layer 3 (Network) handles logical IP addressing and packet routing between networks. Routers operate at this layer."
        ));
        questions.add(new Question(
                "What is the main advantage of UDP over TCP?",
                new String[]{"Guaranteed delivery", "Lower latency and overhead", "Ordered delivery", "Error correction"},
                1,
                "UDP skips connection setup, acknowledgements, and ordering guarantees — this makes it faster and lighter. Ideal for real-time apps like video calls where some packet loss is acceptable."
        ));
        questions.add(new Question(
                "Which HTTP status code means 'resource not found'?",
                new String[]{"200", "301", "404", "500"},
                2,
                "404 Not Found is returned when the server cannot locate the requested resource at the given URL."
        ));
        questions.add(new Question(
                "What does HTTPS add to HTTP?",
                new String[]{"Faster transfer speed", "Compression of data", "TLS/SSL encryption and authentication", "Caching of responses"},
                2,
                "HTTPS = HTTP + TLS. TLS encrypts the data in transit (confidentiality) and authenticates the server via digital certificates (integrity)."
        ));
        questions.add(new Question(
                "What is the TCP 3-way handshake sequence?",
                new String[]{"ACK → SYN → SYN-ACK", "SYN → SYN-ACK → ACK", "SYN → ACK → FIN", "DATA → ACK → FIN"},
                1,
                "TCP establishes a connection with: Client sends SYN, Server replies SYN-ACK, Client confirms with ACK. Only then does data transfer begin."
        ));
        questions.add(new Question(
                "What is the role of DNS?",
                new String[]{"Encrypt web traffic", "Translate domain names to IP addresses", "Route packets between networks", "Assign MAC addresses"},
                1,
                "DNS (Domain Name System) is the Internet's phonebook — it resolves human-readable domain names (e.g., google.com) to machine-readable IP addresses."
        ));
        questions.add(new Question(
                "Which HTTP method is idempotent AND safe?",
                new String[]{"POST", "DELETE", "PUT", "GET"},
                3,
                "GET is both safe (no side effects) and idempotent (same result every time). POST is neither; DELETE and PUT are idempotent but not safe."
        ));
        questions.add(new Question(
                "A firewall primarily protects a network by:",
                new String[]{"Compressing traffic", "Filtering traffic based on rules", "Encrypting all data", "Increasing bandwidth"},
                1,
                "A firewall inspects incoming/outgoing packets and blocks or allows them based on rules (IP address, port, protocol, connection state)."
        ));
        questions.add(new Question(
                "What does NAT (Network Address Translation) do?",
                new String[]{"Encrypts network packets", "Maps private IP addresses to a public IP", "Compresses DNS queries", "Manages TCP connections"},
                1,
                "NAT allows multiple devices on a private network (e.g., 192.168.x.x) to share a single public IP address when communicating with the Internet."
        ));
        questions.add(new Question(
                "Which protocol operates at the Application layer and resolves hostnames?",
                new String[]{"TCP", "IP", "DNS", "ARP"},
                2,
                "DNS operates at the Application layer (Layer 7 of OSI). ARP resolves IPs to MAC addresses (Layer 2), TCP is Layer 4, and IP is Layer 3."
        ));

        c.setQuestions(questions);
        return c;
    }
}
