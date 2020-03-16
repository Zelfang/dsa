import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        char[][] board = new char[][]{
                {'a', 'b'},
                {'c', 'd'},
        };
        String[] words = new String[]{"ab", "cb", "ad", "bd", "ac", "ca", "da", "bc", "db", "adcb", "dabc", "abb", "acb"};
        System.out.println(new Test().findWords(board, words).toString());
    }

    public List<String> findWords(char[][] board, String[] words) {
        Trie trie = new Trie();
        for (String e : words)
            trie.insert(e);
        int height = board.length, width = board[0].length;
        boolean[][] used = new boolean[height][width];
        Set<String> temp = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                dfs2(board, used, height, width, i, j, temp, sb, trie);
            }
        }
        return new LinkedList<>(temp);
    }

    public void dfs(char[][] board, boolean[][] used, int height, int width, int x, int y,
                    Set<String> temp, StringBuilder sb, Trie trie) {
        if (x < 0 || y < 0 || x >= height || y >= width || used[x][y])
            return;
        String pre = sb.append(board[x][y]).toString();
        if (trie.search(pre)) {
            temp.add(pre);
        }
        if (!trie.isPre(pre)) {
            sb.deleteCharAt(sb.length() - 1);
            return;
        }
        used[x][y] = true;
        dfs(board, used, height, width, x + 1, y, temp, sb, trie);
        dfs(board, used, height, width, x - 1, y, temp, sb, trie);
        dfs(board, used, height, width, x, y + 1, temp, sb, trie);
        dfs(board, used, height, width, x, y - 1, temp, sb, trie);
        used[x][y] = false;
        sb.deleteCharAt(sb.length() - 1);
    }

    public void dfs2(char[][] board, boolean[][] used, int height, int width, int x, int y,
                     Set<String> temp, StringBuilder sb, Trie trie) {
        if (x < 0 || y < 0 || x >= height || y >= width || used[x][y])
            return;
        String pre = sb.append(board[x][y]).toString();
        if (trie.search2(trie, board[x][y] - 'a')) {
            temp.add(pre);
        }
        if (!trie.isPre2(trie, board[x][y] - 'a')) {
            sb.deleteCharAt(sb.length() - 1);
            return;
        }
        used[x][y] = true;
        trie = trie.path[board[x][y] - 'a'];
        dfs2(board, used, height, width, x + 1, y, temp, sb, trie);
        dfs2(board, used, height, width, x - 1, y, temp, sb, trie);
        dfs2(board, used, height, width, x, y + 1, temp, sb, trie);
        dfs2(board, used, height, width, x, y - 1, temp, sb, trie);
        used[x][y] = false;
        sb.deleteCharAt(sb.length() - 1);
    }
}

class Trie {
    public Trie[] path;
    private int end;
    private char[] letter;

    public Trie() {
        this.path = new Trie[26];
        this.end = 0;
    }

    public void insert(String str) {
        this.letter = str.toCharArray();
        Trie cur = this;
        for (int i = 0; i < this.letter.length; i++) {
            int index = this.letter[i] - 'a';
            if (cur.path[index] == null) {
                cur.path[index] = new Trie();
            }
            cur = cur.path[index];
        }
        cur.end++;
    }

    public boolean search(String str) {
        this.letter = str.toCharArray();
        Trie cur = this;
        for (int i = 0; i < this.letter.length; i++) {
            int index = this.letter[i] - 'a';
            if (cur.path[index] == null)
                return false;
            cur = cur.path[index];
        }
        if (cur.end == 0)
            return false;
        return true;
    }

    public boolean search2(Trie node, int index) {
        return node.path[index] != null && node.path[index].end > 0;
    }

    public boolean isPre(String str) {
        this.letter = str.toCharArray();
        Trie cur = this;
        for (int i = 0; i < this.letter.length; i++) {
            int index = this.letter[i] - 'a';
            if (cur.path[index] == null)
                return false;
            cur = cur.path[index];
        }
        return true;
    }

    public boolean isPre2(Trie node, int index) {
        return node.path[index] != null;
    }
}