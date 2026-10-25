package com.attendancesystem;

import java.util.ArrayList;
import java.util.Scanner;

// ─────────────────────────────────────────────
//  Student Class
// ─────────────────────────────────────────────
class Student {
    private String rollNumber;
    private String name;
    private int totalClasses;
    private int attendedClasses;

    // Constructor
    public Student(String rollNumber, String name, int totalClasses, int attendedClasses) {
        this.rollNumber    = rollNumber;
        this.name          = name;
        this.totalClasses  = totalClasses;
        this.attendedClasses = attendedClasses;
    }

    // Getters
    public String getRollNumber()    { return rollNumber; }
    public String getName()          { return name; }
    public int    getTotalClasses()  { return totalClasses; }
    public int    getAttendedClasses() { return attendedClasses; }

    // Calculate attendance percentage
    public double calculatePercentage() {
        if (totalClasses == 0) return 0;
        return (attendedClasses * 100.0) / totalClasses;
    }

    // Eligibility check (75% rule)
    public boolean isEligible() {
        return calculatePercentage() >= 75.0;
    }

    // Update attendance
    public void markAttendance(boolean present) {
        totalClasses++;
        if (present) attendedClasses++;
    }

    @Override
    public String toString() {
        return String.format("%-12s %-20s %6d %8d %10.2f%%  %s",
            rollNumber, name, totalClasses, attendedClasses,
            calculatePercentage(), isEligible() ? "ELIGIBLE" : "NOT ELIGIBLE");
    }
}

// ─────────────────────────────────────────────
//  Main Class – AttendanceMonitoring
// ─────────────────────────────────────────────
public class AttendanceMonitoring {

    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre-load sample data
        loadSampleData();

        int choice;
        do {
            printMenu();
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> markAttendance();
                case 3 -> viewAllStudents();
                case 4 -> checkEligibility();
                case 5 -> searchStudent();
                case 6 -> System.out.println("\nThank you! Exiting system...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
    }

    // ── Menu ──────────────────────────────────
    private static void printMenu() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║  Student Attendance Monitoring System    ║");
        System.out.println("║  KG College of Arts and Science          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Add New Student                      ║");
        System.out.println("║  2. Mark Attendance                      ║");
        System.out.println("║  3. View All Students                    ║");
        System.out.println("║  4. Check Eligibility Report             ║");
        System.out.println("║  5. Search Student                       ║");
        System.out.println("║  6. Exit                                 ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ── Add Student ───────────────────────────
    private static void addStudent() {
        System.out.print("\nEnter Roll Number : ");
        String roll = sc.nextLine().trim();

        // Check duplicate
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                System.out.println("Student with this roll number already exists!");
                return;
            }
        }

        System.out.print("Enter Student Name : ");
        String name = sc.nextLine().trim();
        System.out.print("Total Classes Conducted : ");
        int total = sc.nextInt();
        System.out.print("Classes Attended        : ");
        int attended = sc.nextInt();
        sc.nextLine();

        if (attended > total) {
            System.out.println("Error: Attended classes cannot exceed total classes.");
            return;
        }

        students.add(new Student(roll, name, total, attended));
        System.out.println("Student added successfully!");
    }

    // ── Mark Attendance ───────────────────────
    private static void markAttendance() {
        System.out.print("\nEnter Roll Number to mark attendance: ");
        String roll = sc.nextLine().trim();

        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                System.out.print("Was student present? (y/n): ");
                char ch = sc.nextLine().trim().toLowerCase().charAt(0);
                s.markAttendance(ch == 'y');
                System.out.println("Attendance updated. Current: "
                    + String.format("%.2f%%", s.calculatePercentage()));
                return;
            }
        }
        System.out.println("Student not found.");
    }

    // ── View All Students ─────────────────────
    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n" + "─".repeat(75));
        System.out.printf("%-12s %-20s %6s %8s %11s  %s%n",
            "Roll No.", "Name", "Total", "Attended", "Percentage", "Status");
        System.out.println("─".repeat(75));
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("─".repeat(75));
        System.out.println("Total Students: " + students.size());
    }

    // ── Eligibility Report ────────────────────
    private static void checkEligibility() {
        System.out.println("\n════════ ELIGIBILITY REPORT ════════");
        int eligible = 0, notEligible = 0;

        for (Student s : students) {
            if (s.isEligible()) {
                System.out.println("✔ " + s.getName() + " (" + s.getRollNumber() + ") — "
                    + String.format("%.2f%%", s.calculatePercentage()) + " — ELIGIBLE");
                eligible++;
            } else {
                System.out.println("✘ " + s.getName() + " (" + s.getRollNumber() + ") — "
                    + String.format("%.2f%%", s.calculatePercentage()) + " — NOT ELIGIBLE");
                notEligible++;
            }
        }

        System.out.println("─".repeat(40));
        System.out.println("Eligible    : " + eligible);
        System.out.println("Not Eligible: " + notEligible);
        System.out.println("Total       : " + students.size());
    }

    // ── Search Student ────────────────────────
    private static void searchStudent() {
        System.out.print("\nEnter Roll Number or Name to search: ");
        String query = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Student s : students) {
            if (s.getRollNumber().toLowerCase().contains(query)
                    || s.getName().toLowerCase().contains(query)) {
                System.out.println("\n── Student Details ──");
                System.out.println("Roll Number  : " + s.getRollNumber());
                System.out.println("Name         : " + s.getName());
                System.out.println("Total Classes: " + s.getTotalClasses());
                System.out.println("Attended     : " + s.getAttendedClasses());
                System.out.printf ("Percentage   : %.2f%%%n", s.calculatePercentage());
                System.out.println("Eligibility  : " + (s.isEligible() ? "ELIGIBLE ✔" : "NOT ELIGIBLE ✘"));
                found = true;
            }
        }
        if (!found) System.out.println("No student found matching: " + query);
    }

    // ── Sample Data ───────────────────────────
    private static void loadSampleData() {
        students.add(new Student("25BCS023", "Naresh Kumar S",    60, 50));
        students.add(new Student("25BCS024", "Adithya T",     60, 55));
    }
}


