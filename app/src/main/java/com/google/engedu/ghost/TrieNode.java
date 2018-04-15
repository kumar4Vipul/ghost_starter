/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {add(s, 0);}

    private void add(String s, int position) {
        if (position >= s.length()) return;

        char c = s.charAt(position);
        TrieNode n = children.get(c);

        if (n == null) {
            n = new TrieNode();
            children.put(String.valueOf(c), n);
        }

        if (position == s.length() - 1) {
            n.isWord = true;
        }

        n.add(s, position + 1);
    }

    public boolean isWord(String s) {
        return isWord(s, 0);
    }

    public boolean isWord(String s, int position) {
        if (position >= s.length())
            return false;

        char c = s.charAt(position);
        TrieNode n = children.get(c);

        if (n == null) return false;

        if (position == s.length() - 1) {
            return n.isWord;
        }

        return n.isWord(s, position + 1);
    }

    public String getAnyWordStartingWith(String s) {
        return getAnyWordStartingWith(s, 0);
    }

    public String getAnyWordStartingWith(String s, int position) {
        if (position >= s.length()) {
            if (children.size() == 0)
                return null;

            Random r = new Random();
            Object[] chars = children.keySet().toArray();

            return s + chars[r.nextInt(chars.length)];
        }

        char c = s.charAt(position);
        TrieNode n = children.get(c);

        if (n == null) return null;

        return n.getAnyWordStartingWith(s, position + 1);
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}


