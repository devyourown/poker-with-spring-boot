package org.example.domain.player;

import java.util.LinkedList;
import java.util.List;

public class PlayerTable {
    private Node current;
    private Node firstPlayer;
    private int size;

    public PlayerTable(List<Player> players) {
        Node head = new Node(players.get(0));
        Node node = head;
        for (int i=1; i<players.size(); i++) {
            Node newNode = new Node(players.get(i));
            node.setNext(newNode);
            newNode.setPrev(node);
            node = newNode;
        }
        node.setNext(head);
        head.setPrev(node);
        this.current = head;
        this.firstPlayer = head;
        this.size = players.size();
    }

    public void removeSelf() {
        current.prev.setNext(current.getNext());
        current.next.setPrev(current.getPrev());
        if (current == firstPlayer)
            this.firstPlayer = current.getNext();
        this.current = current.getNext();
        size--;
    }

    public Player getCurrentPlayer() {
        return this.current.getPlayer();
    }

    public int getSize() {
        return size;
    }

    public void moveNext() {
        this.current = this.current.getNext();
    }

    public void changeOrder() {
        this.firstPlayer = this.firstPlayer.getNext();
        this.current = this.firstPlayer;
    }
}

class Node {
    Player player;
    Node prev;
    Node next;

    public Node(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return this.next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getPrev() {
        return this.prev;
    }
}
