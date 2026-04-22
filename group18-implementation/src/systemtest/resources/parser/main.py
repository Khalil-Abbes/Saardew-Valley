import json
import math
import matplotlib.pyplot as plt
import matplotlib.patches as patches
from matplotlib.transforms import Affine2D

# Font sizes for tile IDs - adjust as needed
OCTAGON_FONT_SIZE = 15
SQUARE_FONT_SIZE = 7

# Octagon base side length
SIDE = 1
OCT_SIZE = SIDE * (1 + math.sqrt(2))
HALF_SIZE = OCT_SIZE / 2

# Square tile size - replace with your fixed scale if needed
SQUARE_SIZE = (OCT_SIZE / math.sqrt(2)) * 0.59

# Arrow length constant (adjust to change arrow size)
ARROW_LENGTH = SIDE * 0.6

# Color mapping for categories
CATEGORY_COLORS = {
    "FARMSTEAD": "#ffc5aa",       # light red
    "FIELD": "#ffbf00",           # yellow/beige
    "MEADOW": "#d1f6c4",          # light green
    "VILLAGE": "#ffffff",         # white
    "PLANTATION": "#6aa84f",      # green
    "FOREST": "#274e13",          # dark green
    "ROAD": "#616161"             # dark gray
}

# Font color adjustment for dark backgrounds
DARK_BG_CATEGORIES = {"FOREST", "ROAD"}

def get_octagon_points(cx, cy, s):
    a = s / (2 * math.sin(math.pi / 8))
    return [
        (
            cx + a * math.cos(math.radians(22.5 + i * 45)),
            cy + a * math.sin(math.radians(22.5 + i * 45))
        )
        for i in range(8)
    ]

def draw_airflow_arrow(ax, cx, cy, direction_deg):
    # Convert from degrees where 0° is north (up) and increases clockwise
    rad = math.radians(90 - direction_deg)  # Rotate so 0° points up and increases clockwise
    dx = ARROW_LENGTH * math.cos(rad)
    dy = ARROW_LENGTH * math.sin(rad)
    ax.arrow(cx, cy, dx, dy, head_width=0.2, head_length=0.3,
             fc='blue', ec='blue', linewidth=2, length_includes_head=True)

def draw_octagon(ax, cx, cy, tile, tile_id, x, y):
    label = str(tile_id) + "\n" + "(" + str(x) + "," + str(y) + ")"
    category = tile.get("category", "").upper()
    color = CATEGORY_COLORS.get(category, "white")
    points = get_octagon_points(cx, cy, SIDE)
    polygon = patches.Polygon(points, closed=True, edgecolor='black', facecolor=color, linewidth=0.8)
    ax.add_patch(polygon)
    fontcolor = "white" if category in DARK_BG_CATEGORIES else "black"
    ax.text(cx, cy, label, ha='center', va='center', fontsize=OCTAGON_FONT_SIZE, color=fontcolor)
    if tile.get("airflow", False):
        direction = tile.get("direction", 0)
        draw_airflow_arrow(ax, cx, cy, direction)

    # Add plant information
    if tile.get("possiblePlants", False):
        possible = tile.get("possiblePlants", [])
        plants_str = "\n".join(possible)
        ax.text(cx, cy - OCT_SIZE * 0.4, plants_str, ha='center', va='top', fontsize=8, color='black')
    if tile.get("plant", False):
        plant = tile.get("plant", "")
        ax.text(cx, cy + OCT_SIZE * 0.4, plant, ha='center', va='bottom', fontsize=8, color='black')

def draw_rotated_square(ax, cx, cy, tile, tile_id, x, y):
    label = str(tile_id) + "\n" + "(" + str(x) + "," + str(y) + ")"
    category = tile.get("category", "").upper()
    color = CATEGORY_COLORS.get(category, "white")
    size = SQUARE_SIZE
    square = patches.Rectangle(
        (cx - size / 2, cy - size / 2), size, size,
        edgecolor='black', facecolor=color, linewidth=0.8
    )
    t = Affine2D().rotate_deg_around(cx, cy, 45) + ax.transData
    square.set_transform(t)
    ax.add_patch(square)
    fontcolor = "white" if category in DARK_BG_CATEGORIES else "black"
    ax.text(cx, cy, label, ha='center', va='center', fontsize=SQUARE_FONT_SIZE, color=fontcolor)
    if tile.get("airflow", False):
        direction = tile.get("direction", 0)
        draw_airflow_arrow(ax, cx, cy, direction)

    # Add plant information
    if tile.get("possiblePlants", False):
        possible = tile.get("possiblePlants", [])
        plants_str = "\n".join(possible)
        ax.text(cx, cy - SQUARE_SIZE * 0.6, plants_str, ha='center', va='top', fontsize=6, color='black')
    if tile.get("plant", False):
        plant = tile.get("plant", "")
        ax.text(cx, cy + SQUARE_SIZE * 0.6, plant, ha='center', va='bottom', fontsize=6, color='black')

def visualize_tiles(tiles):
    fig, ax = plt.subplots(figsize=(10, 10))
    ax.set_aspect('equal')

    positions = []
    for tile in tiles:
        x = tile['coordinates']['x']
        y = tile['coordinates']['y']
        tile_id = tile['id']
        is_octagon = (x % 2 == 0 and y % 2 == 0)
        cx = x * HALF_SIZE
        cy = -y * HALF_SIZE
        positions.append((cx, cy, tile, tile_id, is_octagon))

    all_x = [p[0] for p in positions]
    all_y = [p[1] for p in positions]
    margin = OCT_SIZE
    ax.set_xlim(min(all_x) - margin, max(all_x) + margin)
    ax.set_ylim(min(all_y) - margin, max(all_y) + margin)

    for cx, cy, tile, tile_id, is_octagon in positions:
        if is_octagon:
            draw_octagon(ax, cx, cy, tile, tile_id, tile['coordinates']['x'], tile['coordinates']['y'])
        else:
            draw_rotated_square(ax, cx, cy, tile, tile_id, tile['coordinates']['x'], tile['coordinates']['y'])

    # Add legend for categories
    legend_elements = []
    for category, color in CATEGORY_COLORS.items():
        fontcolor = "white" if category in DARK_BG_CATEGORIES else "black"
        legend_elements.append(
            patches.Patch(facecolor=color, edgecolor='black', label=category.title())
        )
    ax.legend(
        handles=legend_elements,
        loc='lower center',
        bbox_to_anchor=(0.5, -0.05),
        ncol=3,
        frameon=False,
        fontsize=10
    )

    ax.axis('off')
    plt.tight_layout()
    plt.savefig("tiles_visualization.pdf", format="pdf")
    plt.close()
    print("Saved as tiles_visualization.pdf")

def main():
    with open("mediumMap.json", "r") as f:
        data = json.load(f)
    visualize_tiles(data["tiles"])

if __name__ == "__main__":
    main()
