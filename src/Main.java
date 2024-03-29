import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.HashMap;
import java.util.ArrayList;

class User {
    static HashMap<String, String[]> bookList = new HashMap<>();

    public User() {
        bookList.put("1234", new String[]{"Harry Potter", "J.K. Rowling", "5"});
        bookList.put("5678", new String[]{"Lord of the Rings", "J.R.R. Tolkien", "3"});
    }

    public void displayBooks(HashMap<String, String[]> bookList) {
        System.out.println("=== Daftar Buku ===");
        System.out.println("==============================================================================");
        System.out.printf("|| %-5s || %-30s || %-20s || %-5s ||\n", "ID", "Judul", "Penulis", "Stok");
        System.out.println("==============================================================================");
        for (String bookID : User.bookList.keySet()) {
            String[] book = User.bookList.get(bookID);
            System.out.printf("|| %-5s || %-30s || %-20s || %-5s ||\n", bookID, book[0], book[1], book[2]);
        }
        System.out.println("==============================================================================");
    }

    public void inputBook(HashMap<String, String[]> bookList) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Input Buku ===");
        System.out.println("Pilih jenis buku:");
        System.out.println("1. Story Book");
        System.out.println("2. History Book");
        System.out.println("3. Text Book");
        System.out.print("Masukkan jenis buku yang ingin diinput (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Masukkan judul buku: ");
        String title = scanner.nextLine();
        System.out.print("Masukkan penulis buku: ");
        String author = scanner.nextLine();
        System.out.print("Masukkan stok buku: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        String bookID = Admin.generateId();
        switch (choice) {
            case 1:
                bookList.put(bookID, new String[]{title, author, String.valueOf(stock), "Story Book"});
                break;
            case 2:
                bookList.put(bookID, new String[]{title, author, String.valueOf(stock), "History Book"});
                break;
            case 3:
                bookList.put(bookID, new String[]{title, author, String.valueOf(stock), "Text Book"});
                break;
            default:
                System.out.println("Pilihan tidak valid.");
        }
        System.out.println("Buku berhasil ditambahkan dengan ID: " + bookID);
    }

    void updateBookList() {
        bookList.clear();
        for (Book book : Main.books) {
            String bookID = book.getBookID();
            String title = book.getTitle();
            String author = book.getAuthor();
            int stock = book.getStock();
            String category = book.getCategory();
            bookList.put(bookID, new String[]{title, author, String.valueOf(stock), category});
        }
    }


}



class Student extends User {
    private String name;
    private String nim;
    private String faculty;
    private String studyProgram;
    private ArrayList<String> borrowedBooks;

    public Student(String name, String nim, String faculty, String studyProgram) {
        this.name = name;
        this.nim = nim;
        this.faculty = faculty;
        this.studyProgram = studyProgram;
        this.borrowedBooks = new ArrayList<>();
    }

    public void displayInfo() {
        System.out.println("=== Informasi Mahasiswa ===");
        System.out.println("Nama: " + name);

    }

    public void showBorrowedBooks(HashMap<String, String[]> bookList) {
        System.out.println("=== Buku yang Dipinjam ===");
        for (String bookID : borrowedBooks) {
            String[] book = bookList.get(bookID);
            if (book != null) {
                System.out.printf("\n ID Buku: %s \n Judul: %s \n Penulis: %s \n Durasi Peminjaman: %s hari\n\n", bookID, book[0], book[1], book[3]);
            }
        }
    }


    public void logout() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("Tidak ada buku yang dipinjam. Logging out...");
        } else {
            System.out.println("Anda belum mengembalikan semua buku yang dipinjam.");
            System.out.println("1. Kembali ke menu utama");
            System.out.println("2. Proses pengembalian buku");
            System.out.print("Pilih opsi (1-2): ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Logging out...");
                    break;
                case 2:
                    returnBooks();
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Logging out...");
            }
        }
    }

    public void borrowBook(String bookID, HashMap<String, String[]> bookList) {
        String[] book = bookList.get(bookID);
        if (book != null) {
            int stock = Integer.parseInt(book[2]);
            if (stock > 0) {
                System.out.println("Buku '" + book[0] + "' berhasil dipinjam.");
                stock--;
                book[2] = String.valueOf(stock);
                borrowedBooks.add(bookID);
                System.out.print("Masukkan durasi peminjaman (dalam hari): ");
                int duration = Main.scanner.nextInt();
                bookList.get(bookID)[3] = String.valueOf(duration);
            } else {
                System.out.println("Maaf, stok buku tidak mencukupi.");
            }
        } else {
            System.out.println("Buku dengan ID tersebut tidak ditemukan.");
        }
        updateBookList();
    }


    public void returnBooks() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Mengembalikan Buku ===");
        System.out.println("Buku yang dipinjam:");
        showBorrowedBooks(Main.bookList);
        System.out.print("Masukkan ID Buku yang ingin dikembalikan: ");
        String bookID = scanner.next();
        if (borrowedBooks.contains(bookID)) {
            String[] book = Main.bookList.get(bookID);
            if (book != null) {
                int stock = Integer.parseInt(book[2]);
                stock++;
                book[2] = String.valueOf(stock);
                borrowedBooks.remove(bookID);
                System.out.println("Buku '" + book[0] + "' berhasil dikembalikan.");
            }
        } else {
            System.out.println("Buku dengan ID tersebut tidak dipinjam oleh Anda.");
        }
        updateBookList();
    }
}

class Admin extends User {
    private static final String adminUsername = "riska";
    private static final String password = "riska123";
    public ArrayList<String[]> userStudentList;

    public Admin() {
        userStudentList = new ArrayList<>();
        userStudentList.add(new String[]{"202310370311371", "Riska Deva", "teknik", "informatika"});
    }

    public void addStudent(String name, String nim, String faculty, String studyProgram) {
        if (nim.length() != 15) {
            System.out.println("NIM tidak valid. NIM harus terdiri dari 15 karakter.");
            return;
        }
        userStudentList.add(new String[]{nim, name, faculty, studyProgram});
        System.out.println("Mahasiswa berhasil ditambahkan.");
    }

    @Override
    public void inputBook(HashMap<String, String[]> bookList) {
        super.inputBook(bookList);
    }

    public void displayStudents() {
        System.out.println("=== Daftar Mahasiswa ===");
        for (String[] student : userStudentList) {
            System.out.println("NIM: " + student[0]);
            System.out.println("Nama: " + student[1]);
            System.out.println("Fakultas: " + student[2]);
            System.out.println("Program Studi: " + student[3]);
            System.out.println();
        }
    }

    public void displayBooks(HashMap<String, String[]> bookList) {
        super.displayBooks(bookList);
    }

    public boolean isAdmin(String inputUsername, String inputPassword) {
        return inputUsername.equals(adminUsername) && inputPassword.equals(password);
    }

    public static String generateId() {
        int randomId = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomId);
    }
}

class Book {
    protected String bookID;
    protected String title;
    protected String author;
    protected String category;
    protected int stock;
    protected int duration;

    // Constructor
    public Book(String bookID, String title, String author, String category) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    // Setters
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Getters
    public String getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getStock() {
        return stock;
    }

    public int getDuration() {
        return duration;
    }
}

class HistoryBook extends Book {
    public HistoryBook(String bookID, String title, String author, int stock) {
        super(bookID, title, author, "History Book");
        this.stock = stock;
    }
}

class StoryBook extends Book {
    public StoryBook(String bookID, String title, String author, int stock) {
        super(bookID, title, author, "Story Book");
        this.stock = stock;
    }
}

class TextBook extends Book {
    public TextBook(String bookID, String title, String author, int stock) {
        super(bookID, title, author, "Text Book");
        this.stock = stock;
    }
}

public class Main {
    static HashMap<String, String[]> bookList = new HashMap<>();
    static Admin admin = new Admin();
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Book> books = new ArrayList<>();

    public Main() {
        for (String bookID : bookList.keySet()) {
            String[] bookDetails = bookList.get(bookID);
            String title = bookDetails[0];
            String author = bookDetails[1];
            int stock = Integer.parseInt(bookDetails[2]);
            String category = bookDetails[3];
            Book book;
            switch (category) {
                case "Story Book":
                    book = new StoryBook(bookID, title, author, stock);
                    break;
                case "History Book":
                    book = new HistoryBook(bookID, title, author, stock);
                    break;
                case "Text Book":
                    book = new TextBook(bookID, title, author, stock);
                    break;
                default:
                    book = new Book(bookID, title, author, category);
            }
            books.add(book);
        }
    }

    void menu() {
        System.out.println("=== Menu Login ===:");
        System.out.println("1. Masuk sebagai Mahasiswa");
        System.out.println("2. Masuk sebagai Admin");
        System.out.println("3. Keluar");
    }

    void inputNIM() {
        System.out.print("Masukkan NIM Anda: ");
        String inputNIM = scanner.next();
        checkNIM(inputNIM);
    }

    void checkNIM(String nim) {
        boolean found = false;
        for (String[] user : admin.userStudentList) {
            if (user[0].equals(nim)) {
                found = true;
                System.out.println("Login Mahasiswa Berhasil. Selamat datang, " + user[1] + "!");
                Student student = new Student(user[1], user[0], user[2], user[3]);
                student.displayBooks(bookList);
                menuStudent(student);
                return;
            }
        }
        if (!found)
            System.out.println("Mahasiswa Tidak Ditemukan.");
        menu();
    }

    void menuAdmin() {
        System.out.println("=== Menu Admin ===");
        System.out.println("1. Tambah Mahasiswa");
        System.out.println("2. Input Buku");
        System.out.println("3. Tampilkan Daftar Mahasiswa");
        System.out.println("4. Keluar");
        System.out.print("Pilih opsi (1-4): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                addStudent();
                break;
            case 2:
                admin.inputBook(bookList);
                menuAdmin();
                break;
            case 3:
                admin.displayStudents();
                menuAdmin();
                break;
            case 4:
                menu();
                break;
            default:
                System.out.println("Pilihan Tidak Valid.");
        }
    }

    void addStudent() {
        System.out.println("=== Menambahkan Mahasiswa ===");
        System.out.print("Masukkan Nama Mahasiswa: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Masukkan NIM Mahasiswa: ");
        String nim = scanner.next();
        System.out.print("Masukkan Fakultas Mahasiswa: ");
        String faculty = scanner.next();
        System.out.print("Masukkan Program Studi Mahasiswa: ");
        String studyProgram = scanner.next();

        admin.addStudent(name, nim, faculty, studyProgram);
        menuAdmin();
    }

    void borrowBook(Student student) {
        System.out.println("=== Meminjam Buku ===");
        System.out.print("Masukkan ID Buku yang ingin dipinjam: ");
        String bookID = scanner.next();

        student.borrowBook(bookID, bookList);
        menuStudent(student);
    }

    void returnBook(Student student) {
        student.returnBooks();
        menuStudent(student);
    }

    void menuStudent(Student student) {
        System.out.println("=== Menu Mahasiswa ===");
        System.out.println("1. Pinjam Buku");
        System.out.println("2. Lihat Buku yang Dipinjam");
        System.out.println("3. Mengembalikan Buku");
        System.out.println("4. Keluar");
        System.out.print("Pilih opsi (1-4): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                borrowBook(student);
                break;
            case 2:
                student.showBorrowedBooks(bookList);
                menuStudent(student);
                break;
            case 3:
                returnBook(student);
                break;
            case 4:
                student.logout();
                menu();
                break;
            default:
                System.out.println("Pilihan Tidak Valid.");
                menuStudent(student);
        }
    }

    void logout() {
        System.out.println("Terima kasih telah menggunakan program. Sampai jumpa!");
        scanner.close();
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.menu();

        while (true) {
            System.out.print("Pilih opsi (1-3): ");
            try {
                int opsi = scanner.nextInt();

                switch (opsi) {
                    case 1:
                        System.out.println("Masuk sebagai Mahasiswa:");
                        main.inputNIM();
                        break;
                    case 2:
                        System.out.println("Masuk sebagai Admin:");
                        System.out.print("Masukkan username: ");
                        String inputUsername = scanner.next();
                        System.out.print("Masukkan password: ");
                        String inputPassword = scanner.next();
                        if (admin.isAdmin(inputUsername, inputPassword)) {
                            main.menuAdmin();
                        } else {
                            System.out.println("Admin Tidak Ditemukan.");
                            main.menu();
                        }
                        break;
                    case 3:
                        main.logout();
                        return;
                    default:
                        System.out.println("Pilihan Tidak Valid.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Masukkan harus berupa bilangan bulat.");
                scanner.next();
            }
        }
    }
}