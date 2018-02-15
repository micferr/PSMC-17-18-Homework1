package psmc.hw1;

public class Test {
    public static void main(String[] args) {
        /*String cutoff = "1";
        for (int cu = 0; cu < 7; cu++) {
            System.out.println("Cutoff: " + cutoff);
            StringBuilder n = new StringBuilder("1");
            for (int i = 0; i < (cu<=3?4:8); i++) {
                n.append("0");
                System.out.println("  " + n + " ints");
                System.out.println("    Random:");
                System.out.print("      Seq:          ");
                psmc.hw1.Main.main(new String[]{"--sequential", "-n", n.toString()});
                System.out.print("      Par-SeqMerge: ");
                psmc.hw1.Main.main(new String[]{"--parallel", "-n", n.toString(), "-c", cutoff, "--forks"});
                System.out.print("      Par-ParMerge: ");
                psmc.hw1.Main.main(new String[]{"--psmc.hw1.Parallel", "-n", n.toString(), "-c", cutoff, "--forks"});
                System.out.println("    Decreasing:");
                System.out.print("      Seq:          ");
                psmc.hw1.Main.main(new String[]{"--sequential", "--decreasing", "-n", n.toString()});
                System.out.print("      Par-SeqMerge: ");
                psmc.hw1.Main.main(new String[]{"--parallel", "--decreasing", "-n", n.toString(), "-c", cutoff, "--forks"});
                System.out.print("      Par-ParMerge: ");
                psmc.hw1.Main.main(new String[]{"--psmc.hw1.Parallel", "--decreasing", "-n", n.toString(), "-c", cutoff, "--forks"});
            }
            cutoff += "0";
        }*/
        Main.main(new String[]{"--psmc.hw1.Parallel", "-n", "64", "-c", "1", "--graph", "graph3.dot"});
    }
}
