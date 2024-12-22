package com.cgvsu.objreader;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ObjReader {
	private static final String OBJ_VERTEX_TOKEN = "v";
	private static final String OBJ_TEXTURE_TOKEN = "vt";
	private static final String OBJ_NORMAL_TOKEN = "vn";
	private static final String OBJ_FACE_TOKEN = "f";

	private List<String> lines;
	private Model result;

	private int numVertices;
	private int numTextures;
	private int numNormals;

	public ObjReader(List<String> lines) {
		this.lines = lines;
		this.result = new Model();
		this.numVertices = 0;
		this.numTextures = 0;
		this.numNormals = 0;
	}

	public ObjReader() {
		//пустой конструктор для тестов
	}

	public static ObjReader readLinesFromFile(File fileWithModel) {
		Path pathToTheModelFile = Path.of(fileWithModel.getAbsolutePath());
		try {
			return new ObjReader(Files.readAllLines(pathToTheModelFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static ObjReader readLinesFromPath(Path pathToTheModelFile) {
		try {
			return new ObjReader(Files.readAllLines(pathToTheModelFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static ObjReader readLinesFromString(String rowWithDataModels) {
		return new ObjReader(Collections.singletonList(rowWithDataModels));
	}

	//для тестов пришлось сделать сеттеры
	protected void setNumVertices(int numVertices) {
		this.numVertices = numVertices;
	}

	protected void setNumTextures(int numTextures) {
		this.numTextures = numTextures;
	}

	protected void setNumNormals(int numNormals) {
		this.numNormals = numNormals;
	}

	public Model read() {
		int lineInd = 0;

		for(int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);
			ArrayList<String> wordsInLine = new ArrayList<String>(Arrays.asList(line.split("\\s+")));
			if (wordsInLine.isEmpty()) {
				continue;
			}

			final String token = wordsInLine.get(0);
			wordsInLine.remove(0);

			++lineInd;
			switch (token) {
				case OBJ_VERTEX_TOKEN -> result.getVertices().add(parseVertex(wordsInLine, lineInd));
				case OBJ_TEXTURE_TOKEN -> result.getTextureVertices().add(parseTextureVertex(wordsInLine, lineInd));
				case OBJ_NORMAL_TOKEN -> result.getNormals().add(parseNormal(wordsInLine, lineInd));
				case OBJ_FACE_TOKEN -> result.getPolygons().add(parseFace(wordsInLine, lineInd));
				default -> {}
			}
		}

		return result;
	}

	protected Vector3f parseVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		checkingTheCoordinatesOfTheVertexFor3D(wordsInLineWithoutToken, lineInd);
		try {
			numVertices++;
			return new Vector3f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)),
					Float.parseFloat(wordsInLineWithoutToken.get(2)));

		} catch (NumberFormatException e) {
			throw new ObjReaderException("Failed to parse float value.", lineInd);
		}
	}

	protected Vector2f parseTextureVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		checkingTheCoordinatesOfTheTextureFor2D(wordsInLineWithoutToken, lineInd);
		try {
			numTextures++;
			return new Vector2f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)));

		} catch (NumberFormatException e) {
			throw new ObjReaderException("Failed to parse float value.", lineInd);
		}
	}

	protected Vector3f parseNormal(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		checkingTheCoordinatesOfTheNormalsFor3D(wordsInLineWithoutToken, lineInd);
		try {
			numNormals++;
			return new Vector3f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)),
					Float.parseFloat(wordsInLineWithoutToken.get(2)));

		} catch (NumberFormatException e) {
			throw new ObjReaderException("Failed to parse float value.", lineInd);
		}
	}

	protected Polygon parseFace(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		checkingForTheMinimumNumberOfVerticesForPolygon(wordsInLineWithoutToken, lineInd);

		ArrayList<Integer> onePolygonVertexIndices = new ArrayList<Integer>();
		ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<Integer>();
		ArrayList<Integer> onePolygonNormalIndices = new ArrayList<Integer>();

		for (String s : wordsInLineWithoutToken) {
			parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices, onePolygonNormalIndices, lineInd);
		}

		checkingForDuplicateVertexIndexes(onePolygonVertexIndices, lineInd);

		Polygon result = new Polygon();
		result.setVertexIndices(onePolygonVertexIndices);
		result.setTextureVertexIndices(onePolygonTextureVertexIndices);
		result.setNormalIndices(onePolygonNormalIndices);
		return result;
	}

	protected void parseFaceWord(
			String wordInLine,
			ArrayList<Integer> onePolygonVertexIndices,
			ArrayList<Integer> onePolygonTextureVertexIndices,
			ArrayList<Integer> onePolygonNormalIndices,
			int lineInd) {

		String[] wordIndices = wordInLine.split("/");

		checkingDimensionTheFaceFormat(wordInLine, wordIndices, lineInd);
		checkingIndexValueInObjFormat(wordInLine, wordIndices, lineInd);

		checkingForTheIndicationOfUndefinedVerticesInThePolygon(Integer.parseInt(wordIndices[0]), lineInd);
		switch (wordIndices.length) {
			case 1 -> {
				onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
			}
			case 2 -> {
				onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
				checkingForTheIndicationOfUndefinedTexturesInThePolygon(Integer.parseInt(wordIndices[1]), lineInd);
				onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
			}
			case 3 -> {
				onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
				checkingForTheIndicationOfUndefinedNormalsInThePolygon(Integer.parseInt(wordIndices[2]), lineInd);
				onePolygonNormalIndices.add(Integer.parseInt(wordIndices[2]) - 1);
				if (!wordIndices[1].equals("")) {
					checkingForTheIndicationOfUndefinedTexturesInThePolygon(Integer.parseInt(wordIndices[1]), lineInd);
					onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
				}
			}
		}
	}

	protected void checkingTheCoordinatesOfTheVertexFor3D(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		// проверку возможно как с текстурными надо делать
		if (wordsInLineWithoutToken.size() != 3) {
			throw new ObjReaderException("Invalid number of vertex coordinates.", lineInd);
		}
	}

	protected void checkingTheCoordinatesOfTheTextureFor2D(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		/*if (wordsInLineWithoutToken.size() != 2) {
			throw new ObjReaderException("Invalid number of texture coordinates.", lineInd);
		}*/

		//на случай, если текстурные координаты могут иметь более 2х координат, но равные 0.0
		try {
			if (wordsInLineWithoutToken.size() < 2) {
				throw new ObjReaderException("Invalid number of texture coordinates.", lineInd);
			}
			for (int i = 2; i < wordsInLineWithoutToken.size(); i++) {
				if (Float.parseFloat(wordsInLineWithoutToken.get(i)) != 0.0) {
					throw new ObjReaderException("Invalid number of texture coordinates.", lineInd);
				}
			}
		} catch (NumberFormatException e) {
			throw new ObjReaderException("Failed to parse float value.", lineInd);
		}
	}

	protected void checkingTheCoordinatesOfTheNormalsFor3D(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		// проверку возможно как с текстурными надо делать
		if (wordsInLineWithoutToken.size() != 3) {
			throw new ObjReaderException("Invalid number of normal coordinates.", lineInd);
		}
	}

	protected void checkingForTheMinimumNumberOfVerticesForPolygon(final ArrayList<String> wordsInLineWithoutToken,
																   int lineInd) {
		if (wordsInLineWithoutToken.size() < 3) {
			throw new ObjReaderException("Face must have at least 3 vertices.", lineInd);
		}
	}

	protected void checkingDimensionTheFaceFormat(String wordInLine, String[] wordIndices, int lineInd) {
		if (wordIndices.length < 1 || wordIndices.length > 3) {
			throw new ObjReaderException("Invalid face format: " + wordInLine, lineInd);
		}
	}

	protected void checkingIndexValueInObjFormat(String wordInLine, String[] wordIndices, int lineInd) {
		try {
			for (int i = 0; i < wordIndices.length; i++) {
				if (wordIndices[i] == "") {
					continue;
				}
				if (Integer.parseInt(wordIndices[i]) <= 0) {
					throw new ObjReaderException("Invalid index value in Obj format: " + wordInLine, lineInd);
				}
			}
		} catch (NumberFormatException e) {
			throw new ObjReaderException("Failed to parse int value.", lineInd);
		}
	}

	protected void checkingForDuplicateVertexIndexes(ArrayList<Integer> onePolygonVertexIndices, int lineInd) {
		Set<Integer> vertexIndexSet = new HashSet<>(onePolygonVertexIndices);
		if (vertexIndexSet.size() != onePolygonVertexIndices.size()) {
			throw new ObjReaderException("Face contains duplicate vertex indices.", lineInd);
		}
	}

	private void checkingForTheIndicationOfUndefinedVerticesInThePolygon(int vertexIndex, int lineInd) {
		if (vertexIndex < 1 || vertexIndex > numVertices) {
			throw new ObjReaderException("A nonexistent vertex with index " + vertexIndex + " is added to the polygon. " +
					"The maximum index of the vertex at the moment is " + numVertices, lineInd);
		}
	}

	private void checkingForTheIndicationOfUndefinedTexturesInThePolygon(int textureVertexIndex, int lineInd) {
		if (textureVertexIndex < 1 || textureVertexIndex > numTextures) {
			throw new ObjReaderException("A nonexistent texture vertex with index " + textureVertexIndex +
					" is added to the polygon. The maximum index of the vertex at the moment is " + numTextures, lineInd);
		}
	}

	private void checkingForTheIndicationOfUndefinedNormalsInThePolygon(int normalIndex, int lineInd) {
		if (normalIndex < 1 || normalIndex > numNormals) {
			throw new ObjReaderException("A nonexistent normal with index " + normalIndex + " is added to the polygon. " +
					"The maximum index of the vertex at the moment is " + numNormals, lineInd);
		}
	}
}