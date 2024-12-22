package com.cgvsu.objreader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjReaderFaceTest {
    private final ObjReader objReader = new ObjReader();

    @Test
    public void testParseFaceWord01() {
        objReader.setNumVertices(100); // вынужденная мера, чтобы не выскочили исключения
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        String wordInLine = "1";
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();

        objReader.parseFaceWord(wordInLine, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        ArrayList<Integer> expectedResult = new ArrayList<>(List.of(0));
        Assertions.assertEquals(expectedResult, onePolygonVertexIndices);
    }

    @Test
    public void testParseFaceWord02() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        ArrayList<String> polygon = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();

        for (String s : polygon) {
            objReader.parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        }

        ArrayList<Integer> expectedResult = new ArrayList<>(Arrays.asList(0,1,2,3));
        Assertions.assertEquals(expectedResult, onePolygonVertexIndices);
    }

    @Test
    public void testParseFaceWord03() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        String wordInLine = "3/1";
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();

        objReader.parseFaceWord(wordInLine, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        ArrayList<Integer> expectedResult = new ArrayList<>(List.of(0));
        Assertions.assertEquals(expectedResult, onePolygonTextureVertexIndices);
    }

    @Test
    public void testParseFaceWord04() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        ArrayList<String> polygon = new ArrayList<>(Arrays.asList("1/10", "2/20", "3/30", "4/40"));
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();

        for (String s : polygon) {
            objReader.parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        }

        ArrayList<Integer> expectedResult = new ArrayList<>(Arrays.asList(9,19,29,39));
        Assertions.assertEquals(expectedResult, onePolygonTextureVertexIndices);
    }

    @Test
    public void testParseFaceWord05() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        String wordInLine = "3/1/10";
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();

        objReader.parseFaceWord(wordInLine, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        ArrayList<Integer> expectedResult = new ArrayList<>(List.of(9));
        Assertions.assertEquals(expectedResult, onePolygonNormalIndices);
    }

    @Test
    public void testParseFaceWord06() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        ArrayList<String> polygon = new ArrayList<>(Arrays.asList("1/10/5", "2/20/10", "3/30/15", "4/40/20"));
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();

        for (String s : polygon) {
            objReader.parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        }

        ArrayList<Integer> expectedResult = new ArrayList<>(Arrays.asList(4,9,14,19));
        Assertions.assertEquals(expectedResult, onePolygonNormalIndices);
    }

    @Test
    public void testParseFaceWord07() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        ArrayList<String> polygon = new ArrayList<>(Arrays.asList("1//5", "2//6", "3//7", "4//8"));
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();
        for (String s : polygon) {
            objReader.parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        }
        ArrayList<Integer> expectedResultTexture = new ArrayList<>();

        Assertions.assertEquals(expectedResultTexture, onePolygonTextureVertexIndices);
    }

    @Test
    public void testParseFaceWord08() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        ArrayList<String> polygon = new ArrayList<>(Arrays.asList("1//5", "2//6", "3//7", "4//8"));
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();
        for (String s : polygon) {
            objReader.parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices,onePolygonNormalIndices, 10);

        }
        ArrayList<Integer> expectedResultNormal = new ArrayList<>(Arrays.asList(4,5,6,7));

        Assertions.assertEquals(expectedResultNormal, onePolygonNormalIndices);
    }

    @Test
    public void testCheckingForTheMinimumNumberOfVerticesForPolygon() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        try {
            ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(List.of("1/10/5"));
            objReader.checkingForTheMinimumNumberOfVerticesForPolygon(wordsInLineWithoutToken, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Face must have at least 3 vertices.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckingDimensionTheFaceFormat() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        try {
            String wordInLine = "1/10/5/4";
            String[] wordIndices = wordInLine.split("/");
            objReader.checkingDimensionTheFaceFormat(wordInLine, wordIndices, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Invalid face format: 1/10/5/4";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckingIndexValueInObjFormat() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        try {
            String wordInLine = "1/-10/5/4";
            String[] wordIndices = wordInLine.split("/");
            objReader.checkingIndexValueInObjFormat(wordInLine, wordIndices, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Invalid index value in Obj format: 1/-10/5/4";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckingForDuplicateVertexIndexes() {
        objReader.setNumVertices(100);
        objReader.setNumTextures(100);
        objReader.setNumNormals(100);

        try {
            ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>(Arrays.asList(4,5,6,4));
            objReader.checkingForDuplicateVertexIndexes(onePolygonVertexIndices, 10);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Face contains duplicate vertex indices.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }
}