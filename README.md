# dcel-planar-subdivision
# Point Location in Planar Subdivisions (Slab Method)

## 1. Requirement

The project focuses on the **Point Location Problem** within a **Planar Straight Line Graph (PSLG)**. Given a subdivision of the plane represented by a **Doubly Connected Edge List (DCEL)**, the goal is to identify which face contains a query point **M**.

### Data Structure (DCEL)
The graph is defined by vertices and half-edges. For each edge, the following attributes are provided:
* **V1, V2:** Coordinates of the start and end vertices.
* **F1, F2:** Indices of the Left and Right faces (Face 0 represents the unbounded external face).
* **P1, P2:** Successor edges in a counter-clockwise rotation around V1 and V2, respectively.

---

## 2. Methodology: The Slab Method

To achieve efficient point location, the algorithm uses a **Pre-processing + Binary Search** approach known as the **Slab Method**.

### Phase I: Pre-processing
1. **Edge Normalization:** Edges are oriented consistently (usually by ensuring $y(V_1) \le y(V_2)$), swapping DCEL fields ($F_1 \leftrightarrow F_2$, $P_1 \leftrightarrow P_2$) where necessary.
2. **Vertical Ordering:** Vertices are sorted by their Y-coordinates to define horizontal "slabs".
3. **Plane Sweep:** A sweep-line moves vertically. At each vertex (event), the set of "active" edges intersecting the current slab is updated.

### Phase II: Point Localization
To find the face containing point **M(x_M, y_M)**:
1. **Vertical Search:** Perform a binary search on the sorted Y-coordinates of the vertices to find the **slab** containing $y_M$.
2. **Horizontal Search:** Within that slab, perform a binary search on the active edges to find the two edges that horizontally flank $x_M$.
3. **Face Identification:** The identified edges and their associated DCEL records determine the specific face containing point **M**.

---

## 3. Practical Example

### PSLG Data (Edges)
| Edge | V1 (x, y) | V2 (x, y) | F1 (Left) | F2 (Right) | P1 | P2 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | (0, 0) | (15, 2) | 1 | 0 | 5 | 2 |
| 2 | (15, 2) | (15, 15) | 1 | 0 | 1 | 3 |
| 3 | (15, 15) | (2, 13) | 2 | 0 | 5 | 4 |
| 4 | (2, 13) | (0, 0) | 2 | 0 | 3 | 1 |
| 5 | (0, 0) | (15, 15) | 2 | 1 | 4 | 2 |

### Algorithm Execution
* **Normalization:** Ensures all edges are processed from bottom to top.
* **Slabs:** Horizontal regions are created at $y=0, y=2, y=13, y=15$.
* **Query:** For any point **M**, the algorithm identifies the Y-slab, then performs an orientation test against the edges in that slab to output the correct **Face ID**.
