package com.cgvsu.objreader;

import com.cgvsu.math.Vector2f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjReaderTextureVertexTest {
    private final ObjReader objReader = new ObjReader();

    @Test
    public void testParseTextureVertex01() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.01", "1.02"));
        Vector2f result = objReader.parseTextureVertex(wordsInLineWithoutToken, 5);
        Vector2f expectedResult = new Vector2f(1.01f, 1.02f);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void testParseTextureVertex02() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.01", "1.02"));
        Vector2f result = objReader.parseTextureVertex(wordsInLineWithoutToken, 5);
        Vector2f expectedResult = new Vector2f(1.10f, 1.02f);
        Assertions.assertFalse(result.equals(expectedResult));
    }

    @Test
    public void testParseTextureVertex03() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.01", "1.02"));
        Vector2f result = objReader.parseTextureVertex(wordsInLineWithoutToken, 5);
        Vector2f expectedResult = new Vector2f(1.01f, 1.10f);
        Assertions.assertFalse(result.equals(expectedResult));
    }

    @Test
    public void testParseTextureVertex04() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("abo", "ba"));
        try {
            objReader.parseTextureVertex(wordsInLineWithoutToken, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Failed to parse float value.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckingTheCoordinatesOfTheTextureFor2D01() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.0", "2.0"));
        try {
            objReader.checkingTheCoordinatesOfTheTextureFor2D(wordsInLineWithoutToken, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Invalid number of texture coordinates.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckingTheCoordinatesOfTheTextureFor2D02() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.0", "2.0", "0.0"));
        try {
            objReader.checkingTheCoordinatesOfTheTextureFor2D(wordsInLineWithoutToken, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Invalid number of texture coordinates.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckingTheCoordinatesOfTheTextureFor2D03() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.0"));
        try {
            objReader.checkingTheCoordinatesOfTheTextureFor2D(wordsInLineWithoutToken, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Invalid number of texture coordinates.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckingTheCoordinatesOfTheTextureFor2D04() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.0", "2.0", "3.0"));
        try {
            objReader.checkingTheCoordinatesOfTheTextureFor2D(wordsInLineWithoutToken, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Invalid number of texture coordinates.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }
}