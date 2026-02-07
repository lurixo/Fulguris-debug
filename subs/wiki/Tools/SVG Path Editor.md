# SVG Path Editor

## Overview

**SVG Path Editor** is a free, web-based tool for creating and editing SVG path data. It provides a visual interface for designing vector paths, making it invaluable for quickly modifying icons and vector graphics used in Fulguris.

**Website:** https://yqnn.github.io/svg-path-editor/
**GitHub:** https://github.com/Yqnn/svg-path-editor
**License:** MIT License

## Why SVG Path Editor for Fulguris

Fulguris uses vector icons throughout the UI for scalability and theme support. SVG Path Editor enables quick icon modifications without needing heavy design software:

- **Browser-Based:** No installation required, works directly in browser
- **Visual Feedback:** See path changes in real-time as you edit
- **Path Commands:** Directly edit SVG path data with `M`, `L`, `C`, `Z` commands
- **Control Points:** Visual manipulation of Bézier curve control points
- **Export:** Copy edited path data directly into Android vector drawables
- **Free and Open Source:** No licensing costs or restrictions

## Use Cases for Fulguris

### 1. Icon Modifications

**Common Scenarios:**
- Adjust icon size or proportions
- Tweak icon details (rounded corners, stroke width)
- Simplify complex paths for better rendering
- Mirror or flip icons
- Combine elements from multiple icons

### 2. Creating New Icons

**When to Use:**
- Quick custom icons for new features
- Prototype icon designs before final artwork
- Simple geometric icons (circles, arrows, checkmarks)

### 3. Converting Between Formats

**Format Support:**
- SVG `<path>` elements
- Android VectorDrawable `pathData`
- CSS path data
- Plain SVG path strings

## Getting Started

### Access the Tool

1. Open browser and visit: https://yqnn.github.io/svg-path-editor/
2. No account or installation needed
3. Tool loads instantly

### Interface Overview

**Main Areas:**
- **Canvas (Center):** Visual preview of the path
- **Path Data Input (Top):** Text field for SVG path commands
- **Control Panel (Left):** Grid, snap, and view controls
- **Path Commands (Right):** List of individual path commands

## Working with Fulguris Icons

### Extracting Path from Android VectorDrawable

Android vector drawables are located in: `app/src/main/res/drawable/`

**Example - Edit `ic_action_star.xml`:**

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="@color/icon_color"
        android:pathData="M12,17.27L18.18,21l-1.64,-7.03L22,9.24l-7.19,-0.61L12,2 9.19,8.63 2,9.24l5.46,4.73L5.82,21z"/>
</vector>
```

**Extract the path:**
1. Copy the `android:pathData` value:
   ```
   M12,17.27L18.18,21l-1.64,-7.03L22,9.24l-7.19,-0.61L12,2 9.19,8.63 2,9.24l5.46,4.73L5.82,21z
   ```

2. Paste into SVG Path Editor input field

3. Path appears visually on canvas

### Modifying the Path

**Visual Editing:**
1. Click on path control points (blue circles)
2. Drag to move points
3. Shift+click to add new points
4. Delete key to remove selected point
5. Use Bézier handles (green circles) for curves

**Text Editing:**
1. Edit path commands directly in input field
2. Changes reflect immediately on canvas
3. Use path command reference (see below)

**Common Modifications:**
- **Scale:** Multiply all numeric values by scaling factor
- **Translate:** Add offset to all coordinate pairs
- **Mirror Horizontally:** Negate all X coordinates
- **Mirror Vertically:** Negate all Y coordinates

### Saving Back to Android

1. Copy modified path data from editor
2. Paste back into `android:pathData` attribute
3. Save the XML file
4. Preview in Android Studio

**Example - Modified star:**
```xml
<path
    android:fillColor="@color/icon_color"
    android:pathData="M12,15.27L18.18,19l-1.64,-7.03L22,8.24l-7.19,-0.61L12,1 9.19,7.63 2,8.24l5.46,4.73L5.82,19z"/>
```

## SVG Path Commands Reference

### Basic Commands

| Command | Description | Syntax | Example |
|---------|-------------|--------|---------|
| `M` | Move to (absolute) | `M x y` | `M10,20` |
| `m` | Move to (relative) | `m dx dy` | `m5,5` |
| `L` | Line to (absolute) | `L x y` | `L30,40` |
| `l` | Line to (relative) | `l dx dy` | `l10,10` |
| `H` | Horizontal line (absolute) | `H x` | `H50` |
| `h` | Horizontal line (relative) | `h dx` | `h20` |
| `V` | Vertical line (absolute) | `V y` | `V60` |
| `v` | Vertical line (relative) | `v dy` | `v15` |
| `Z` / `z` | Close path | `Z` | `Z` |

### Curve Commands

| Command | Description | Syntax | Example |
|---------|-------------|--------|---------|
| `C` | Cubic Bézier (absolute) | `C x1 y1 x2 y2 x y` | `C10,20 30,40 50,60` |
| `c` | Cubic Bézier (relative) | `c dx1 dy1 dx2 dy2 dx dy` | `c5,5 10,10 15,15` |
| `S` | Smooth cubic Bézier (absolute) | `S x2 y2 x y` | `S30,40 50,60` |
| `s` | Smooth cubic Bézier (relative) | `s dx2 dy2 dx dy` | `s10,10 15,15` |
| `Q` | Quadratic Bézier (absolute) | `Q x1 y1 x y` | `Q10,20 30,40` |
| `q` | Quadratic Bézier (relative) | `q dx1 dy1 dx dy` | `q5,5 10,10` |
| `T` | Smooth quadratic Bézier (absolute) | `T x y` | `T30,40` |
| `t` | Smooth quadratic Bézier (relative) | `t dx dy` | `t10,10` |

### Arc Commands

| Command | Description | Syntax | Example |
|---------|-------------|--------|---------|
| `A` | Arc (absolute) | `A rx ry rotation large-arc sweep x y` | `A10,10 0 0,1 30,40` |
| `a` | Arc (relative) | `a rx ry rotation large-arc sweep dx dy` | `a10,10 0 0,1 20,20` |

**Arc Parameters:**
- `rx`, `ry` - Radius X and Y
- `rotation` - X-axis rotation in degrees
- `large-arc` - 0 = small arc, 1 = large arc
- `sweep` - 0 = counter-clockwise, 1 = clockwise
- `x`, `y` - End point coordinates

## Common Icon Editing Patterns

### Pattern 1: Scaling an Icon

**Problem:** Icon is too large/small for viewport

**Solution:**
1. Note viewport size (e.g., 24x24)
2. Calculate scale factor (e.g., 0.8 for 80%)
3. Multiply all coordinate values by scale factor

**Example:**
```
Original: M12,2L14,8
Scaled (0.8x): M9.6,1.6L11.2,6.4
```

### Pattern 2: Centering an Icon

**Problem:** Icon is off-center in viewport

**Solution:**
1. Find icon bounds (min/max X and Y)
2. Calculate center offset
3. Add offset to all coordinates

**Example:**
```
Icon bounds: 2,2 to 18,18 (size: 16x16)
Viewport: 24x24
Offset: (24-16)/2 = 4

Original: M2,2L18,18
Centered: M6,6L22,22
```

### Pattern 3: Mirroring an Icon

**Problem:** Icon faces wrong direction

**Solution - Horizontal flip:**
1. Find viewport width (e.g., 24)
2. For each X coordinate: `newX = viewportWidth - oldX`

**Example:**
```
Viewport: 24
Original: M4,12L20,12
Mirrored: M20,12L4,12
```

### Pattern 4: Rounding Corners

**Problem:** Sharp corners need softening

**Solution:**
1. Replace `L` commands with `Q` (quadratic curves)
2. Add control point slightly offset from corner

**Example:**
```
Original: M10,10L20,10L20,20
Rounded: M10,10L18,10Q20,10 20,12L20,20
```

### Pattern 5: Simplifying Paths

**Problem:** Path has redundant or tiny segments

**Solution:**
1. Remove near-duplicate points
2. Merge colinear line segments
3. Replace complex curves with simpler ones

**Example:**
```
Complex: M10,10L10.1,10.1L10.2,10.2L20,20
Simple: M10,10L20,20
```

## Integration with Development Workflow

### Typical Workflow

1. **Identify icon to modify** - Browse `app/src/main/res/drawable/ic_*.xml`
2. **Extract path data** - Copy `android:pathData` attribute
3. **Open SVG Path Editor** - https://yqnn.github.io/svg-path-editor/
4. **Paste path** - Into input field
5. **Make modifications** - Visual or text editing
6. **Test in viewport** - Adjust grid to match Android viewport
7. **Copy modified path** - From input field
8. **Update XML file** - Paste into `android:pathData`
9. **Preview in Android Studio** - Check rendering
10. **Build and test** - Verify on device

### Testing Icon Changes

**In Android Studio:**
1. Open drawable XML in Android Studio
2. Preview pane shows icon rendering
3. Test with different themes (light/dark)
4. Check multiple densities (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)

**On Device:**
1. Build and install app
2. Navigate to screens using the icon
3. Test on different screen sizes
4. Verify in both light and dark mode

## Tips and Tricks

### Precision Editing
- **Enable grid:** Use snap-to-grid for precise alignment
- **Zoom in/out:** Mouse wheel for detailed editing
- **Coordinate display:** Shows exact position of cursor

### Efficiency
- **Copy path segments:** Reuse common shapes
- **Use relative commands:** Easier to adjust and scale
- **Simplify when possible:** Fewer commands = better performance

### Quality
- **Match viewport:** Ensure path fits within intended viewport
- **Test at small sizes:** Icons often render at 24dp or less
- **Avoid sub-pixel values:** Use integers when possible for crisp rendering

### Common Pitfalls
- ⚠️ **Forgetting `Z` command:** May cause unclosed paths
- ⚠️ **Decimal precision:** Too many decimals can bloat file size
- ⚠️ **Viewport mismatch:** Path outside viewport won't be visible
- ⚠️ **Absolute vs relative:** Mixing can cause confusion

## Alternatives and When to Use Them

### When SVG Path Editor Is Best
- Quick icon tweaks
- Simple geometric shapes
- Learning SVG path syntax
- No installation wanted

### When to Use Full Vector Editor
- **[Affinity Designer](/Slion/Fulguris/wiki/Affinity-Designer)** - Complex icons, original artwork
- **Inkscape** - Advanced path operations, effects
- **Adobe Illustrator** - Professional design work

### When to Use Material Icons
- **Material Design Icons** - Pre-made icons following Material guidelines
- **Website:** https://fonts.google.com/icons
- Often better to use existing icons than create new ones

## Troubleshooting

### Path Not Rendering in Android

**Problem:** Path shows in editor but not in Android

**Possible Causes:**
1. **Viewport mismatch:** Path coordinates outside Android `viewportWidth`/`viewportHeight`
2. **Syntax error:** Check for typos in path commands
3. **Color not set:** Ensure `android:fillColor` or `android:strokeColor` is defined

**Solution:** Verify XML syntax and viewport bounds

### Path Looks Different in Android

**Problem:** Icon renders differently than in editor

**Possible Causes:**
1. **Fill rule:** Android uses default fill rule (non-zero)
2. **Stroke width:** May not match preview
3. **Theme colors:** Icon color changes with theme

**Solution:** Test with actual theme colors and stroke settings

### Can't Edit Complex Paths

**Problem:** Too many control points to manage

**Solution:** Use [Affinity Designer](/Slion/Fulguris/wiki/Affinity-Designer) for complex editing, then export path

## Related Documentation

- [Affinity Designer](/Slion/Fulguris/wiki/Affinity-Designer) - Professional vector graphics editing
- [Android Studio](/Slion/Fulguris/wiki/Android-Studio) - Preview icons in context
- [Design/Icons](/Slion/Fulguris/wiki/Icons) - Icon design guidelines for Fulguris

## External Links

- **SVG Path Editor:** https://yqnn.github.io/svg-path-editor/
- **GitHub Repository:** https://github.com/Yqnn/svg-path-editor
- **SVG Path Specification:** https://www.w3.org/TR/SVG/paths.html
- **Android VectorDrawable:** https://developer.android.com/reference/android/graphics/drawable/VectorDrawable
- **Material Design Icons:** https://fonts.google.com/icons

---

**Last Updated:** December 21, 2025
**Maintained by:** Fulguris Development Team
