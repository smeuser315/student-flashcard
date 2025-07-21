package com.example.it404.Flashcard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeckModel implements Serializable {

    String Title, Author, UserID, DeckID, CourseCode;
    List<List<String>> cards = new ArrayList<>();

    public DeckModel() {
    }

    public DeckModel(String title, String author, String userID, String deckID, String courseCode, List<List<String>>  cards) {
        Title = title;
        Author = author;
        UserID = userID;
        DeckID = deckID;
        CourseCode = courseCode;
        this.cards = cards;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDeckID() {
        return DeckID;
    }

    public void setDeckID(String deckID) {
        DeckID = deckID;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }

    public List<List<String>>  getCards() {
        return cards;
    }

    public void setCards(List<List<String>> cards) {
        this.cards = cards;
    }
}
