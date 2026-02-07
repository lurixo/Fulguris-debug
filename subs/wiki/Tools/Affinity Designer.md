# Affinity Designer

## Overview

**Affinity Designer** is a professional vector graphics editor used for creating and editing icons, logos, and visual assets for Fulguris. It provides a comprehensive toolset for precise vector design work, serving as an alternative to Adobe Illustrator.

**Website:** https://affinity.serif.com/en-us/designer/  
**License:** Commercial (one-time purchase, no subscription)  
**Platforms:** Windows, macOS, iPad

## Why Affinity Designer for Fulguris

Affinity Designer is chosen for Fulguris development due to its professional capabilities without ongoing subscription costs:

- **Professional Vector Tools:** Full-featured Pen tool, Node tool, Boolean operations
- **One-Time Purchase:** No recurring subscription fees (unlike Adobe Illustrator)
- **Format Support:** SVG import/export, PNG export at multiple resolutions
- **Precision:** Pixel-perfect alignment and grid snapping
- **Performance:** Fast rendering and responsive interface
- **Non-Destructive:** Layer effects and adjustments that can be modified later
- **Artboards:** Multiple icon variations in one document
- **Export Personas:** Specialized workspace for exporting assets

## Use Cases for Fulguris

### 1. Icon Design

**Creating Original Icons:**
- Design custom icons for new features
- Maintain consistent visual style across icon set
- Create variations (outlined, filled, different states)
- Export multiple density versions for Android (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)

**Examples:**
- Tab management icons
- Session icons
- Settings icons
- Action bar icons
- Notification icons

### 2. Logo and Branding

**Brand Assets:**
- Fulguris app icon (launcher icon)
- Splash screen graphics
- Promotional images
- Website graphics
- Social media assets

### 3. App Store Graphics

**Store Listing Assets:**
- Feature graphics (1024x500)
- Screenshots with overlays
- Promotional banners
- Icon backgrounds

### 4. Complex Vector Editing

**Advanced Operations:**
- Combining multiple paths with Boolean operations
- Gradient and transparency effects
- Text to vector conversion
- Complex shape transformations
- Symbol libraries for reusable elements

## Installation

### System Requirements

**Minimum:**
- Windows 10 (64-bit) or macOS 10.15+
- 4 GB RAM (8 GB recommended)
- 1 GB free disk space
- 1280 x 768 display

**Recommended:**
- 16 GB RAM for complex documents
- GPU with DirectX 12 (Windows) or Metal (macOS)
- High-resolution display for detailed work
- Graphics tablet (optional, for drawing)

### Purchase and Install

1. **Visit:** https://affinity.serif.com/en-us/designer/
2. **Purchase license** - One-time payment (typically $69.99, sales often available)
3. **Download installer** for your platform
4. **Run installer** and enter license key
5. **Launch application**

**Free Trial:**
- 30-day free trial available before purchase
- Full functionality during trial period

## Interface Overview

### Personas (Workspaces)

Affinity Designer has specialized workspaces called "Personas":

**Designer Persona (Default):**
- Vector drawing and editing
- Main workspace for icon creation
- Full toolbox access

**Pixel Persona:**
- Raster editing capabilities
- Fine-tune pixel details
- Paint and retouch tools

**Export Persona:**
- Batch export assets
- Create export slices
- Generate multiple resolutions

**Switch Personas:**
- Top-left corner icons or `Ctrl+,` (Designer), `Ctrl+.` (Pixel), `Ctrl+/` (Export)

### Key Tools for Icon Design

**Essential Tools:**
- **Pen Tool (P):** Create precise vector paths
- **Node Tool (A):** Edit path nodes and curves
- **Rectangle/Ellipse Tool (M/N):** Basic shapes
- **Corner Tool:** Round shape corners
- **Boolean Operations:** Add, Subtract, Intersect, Divide
- **Align Tools:** Precise object alignment
- **Transform Panel:** Exact size and position values

## Creating Icons for Fulguris

### Setting Up Icon Document

1. **New Document:**
   - File → New (Ctrl+N)
   - Document Type: Web
   - Page Width: 24 px (or 512 px for high-res editing)
   - Page Height: 24 px (or 512 px)
   - DPI: 72 (for web/mobile)
   - Color Format: RGB/8

2. **Enable Pixel Grid:**
   - View → Show Pixel Grid (when zoomed in)
   - View → Snap to Pixel Grid
   - Ensures crisp rendering at small sizes

3. **Setup Artboards (Optional):**
   - Create multiple artboards for icon variations
   - View → Studio → Artboards
   - Useful for comparing light/dark versions

### Icon Design Workflow

#### Step 1: Sketch Basic Shape

```
Tools: Rectangle (M), Ellipse (N), or Pen (P)
```

1. Draw basic shape with primary tool
2. Use **Shift** to constrain proportions
3. Hold **Alt** to draw from center

#### Step 2: Refine with Node Tool

```
Tool: Node Tool (A)
```

1. Select shape with Node Tool
2. Adjust control points for curves
3. Convert corners to smooth curves (double-click node)
4. Add nodes by clicking on path
5. Delete nodes by selecting and pressing Delete

#### Step 3: Apply Boolean Operations

```
Operations: Add, Subtract, Intersect, Divide, XOR
```

1. Select multiple shapes
2. Layer → Geometry → Boolean Operation
3. Choose operation type
4. Creates compound shape

**Example - Creating a Star with Hole:**
1. Create star shape
2. Create smaller circle in center
3. Select both shapes
4. Layer → Geometry → Subtract

#### Step 4: Add Details

```
Effects: Stroke, Fill, Gradients, Shadows
```

1. **Stroke:** Appearance → Stroke panel
   - Set stroke width (e.g., 2px)
   - Choose stroke alignment (center, inside, outside)
2. **Fill:** Appearance → Fill panel
   - Solid color, gradient, or pattern
3. **Effects:** Layer → Layer Effects
   - Outer shadow, inner shadow, glow, etc.

#### Step 5: Optimize for Android

**Simplification:**
1. Merge paths when possible (reduce complexity)
2. Convert strokes to fills if needed
3. Remove invisible or redundant elements
4. Ensure proper ordering (background to foreground)

**Viewport Check:**
1. Zoom to 100% or actual size
2. Verify icon is recognizable at 24x24 pixels
3. Check contrast and visibility

### Exporting Icons for Android

#### Method 1: Export SVG for Vector Drawable

1. Select icon elements
2. File → Export
3. Format: SVG
4. Preset: SVG (for export)
5. Advanced options:
   - Flatten transforms: ✓
   - Set viewBox: ✓
   - Responsive: ✓ (for percentage sizing)
6. Export

**Post-Export:**
1. Open SVG in text editor
2. Extract `<path>` `d` attribute value
3. Paste into Android `android:pathData`

**Example SVG:**
```xml
<svg viewBox="0 0 24 24">
  <path d="M12,2 L18.18,8.63 L22,9.24 L17,14 L18.18,21 L12,17.27 Z"/>
</svg>
```

**Android VectorDrawable:**
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="@color/icon_color"
        android:pathData="M12,2 L18.18,8.63 L22,9.24 L17,14 L18.18,21 L12,17.27 Z"/>
</vector>
```

#### Method 2: Export PNG for Raster Icons

**For Launcher Icons and High-Res Assets:**

1. **Switch to Export Persona** (`Ctrl+/`)
2. **Create Slice:**
   - Click "Create Slice" button
   - Draw rectangle around icon
   - Name slice (e.g., "ic_launcher")
3. **Configure Export:**
   - Select slice in Slices panel
   - Add export format: PNG
   - Set scale for multiple densities:
     - 1x (mdpi) - 48x48 px
     - 1.5x (hdpi) - 72x72 px
     - 2x (xhdpi) - 96x96 px
     - 3x (xxhdpi) - 144x144 px
     - 4x (xxxhdpi) - 192x192 px
4. **Export All:**
   - File → Export Slices
   - Choose output folder
   - Maintains folder structure for Android

#### Method 3: Batch Export with Artboards

1. Create artboards for each density (24, 36, 48, 72, 96 px)
2. Place icon in each artboard
3. File → Export
4. Select "All Artboards"
5. Choose format and settings
6. Export creates separate file for each artboard

## Advanced Techniques

### Creating Symbol Libraries

**Purpose:** Reusable icon elements across documents

1. **Create Symbol:**
   - Select elements
   - Right-click → Create Symbol
   - Name symbol (e.g., "rounded_corner")
2. **Use Symbol:**
   - View → Studio → Symbols
   - Drag symbol from panel into document
3. **Update Symbol:**
   - Edit one instance
   - All instances update automatically

**Use Cases:**
- Common shapes (checkmarks, arrows)
- Repeated elements (background shapes)
- Style variations (button backgrounds)

### Grid and Guide Setup

**Pixel Grid for 24x24 Icons:**
1. View → Grid and Axis Manager
2. Set grid spacing: 1 px
3. Enable: View → Show Grid
4. Enable: View → Snap to Grid

**Guides for Alignment:**
1. Drag from rulers (View → Show Rulers)
2. Place guides at 2px, 4px, 20px, 22px (safe margins)
3. Align key elements to guides

### Boolean Operations Deep Dive

**Add (Union):**
- Combines overlapping shapes into one
- Use for merging elements

**Subtract:**
- Removes one shape from another
- Use for cutting holes

**Intersect:**
- Keeps only overlapping area
- Use for clipping shapes

**Divide:**
- Splits shapes at intersections
- Use for creating separate elements from overlaps

**XOR (Exclude):**
- Removes overlapping areas
- Use for complex cutouts

### Working with Text

**Converting Text to Vector:**
1. Create text with Text Tool (T)
2. Right-click → Convert to Curves
3. Now editable as vector paths
4. Use for logo text and custom lettering

## Tips and Best Practices

### Icon Design Guidelines

**Consistency:**
- Use consistent stroke width across icon set
- Maintain similar visual weight
- Follow Material Design guidelines (when applicable)
- 2px stroke width is common for 24x24 icons

**Simplicity:**
- Minimize detail at small sizes
- Focus on recognizable silhouettes
- Avoid fine lines that disappear when scaled down
- Test at actual display size (24dp)

**Accessibility:**
- Ensure sufficient contrast
- Avoid relying solely on color
- Test with color blindness simulators
- Provide alternate text descriptions

### Performance Optimization

**Path Simplification:**
- Delete unnecessary nodes
- Merge overlapping paths
- Convert complex shapes to simpler forms
- Avoid excessive Bézier curves

**File Size:**
- Export only necessary elements
- Remove hidden or unused layers before export
- Flatten effects when possible
- Use simple gradients or solid fills

### Workflow Efficiency

**Keyboard Shortcuts:**
- `Ctrl+C` / `Ctrl+V` - Copy/Paste
- `Ctrl+D` - Duplicate
- `Ctrl+G` - Group
- `Ctrl+Shift+G` - Ungroup
- `V` - Move Tool
- `A` - Node Tool
- `P` - Pen Tool
- `M` - Rectangle Tool
- `Ctrl+;` - Show/Hide Guides

**Layers Organization:**
- Name layers descriptively ("icon_foreground", "background")
- Group related elements
- Lock background layers to prevent accidental edits
- Use color coding for layer groups

## Integration with Android Development

### Workflow: Affinity → Android Studio

1. **Design in Affinity Designer:**
   - Create icon at high resolution (512x512 or 24x24)
   - Refine details, colors, effects

2. **Export as SVG:**
   - File → Export → SVG
   - Extract path data from SVG

3. **Create VectorDrawable:**
   - Android Studio → New → Vector Asset → Local file (import SVG)
   - Or manually create XML with path data

4. **Test in App:**
   - Build and run app
   - Check icon rendering
   - Test in light/dark themes

5. **Iterate:**
   - Adjust in Affinity Designer if needed
   - Re-export and update Android asset
   - Repeat until satisfied

### Launcher Icon Workflow

1. **Design at 512x512:**
   - Create launcher icon at high resolution
   - Include background layer and foreground layer

2. **Export Adaptive Icon Layers:**
   - Background layer: 108x108 dp safe zone
   - Foreground layer: 108x108 dp safe zone
   - Keep important content within 66dp circle

3. **Generate Density Variants:**
   - Use Android Asset Studio or export from Affinity
   - mdpi: 48x48, hdpi: 72x72, xhdpi: 96x96, xxhdpi: 144x144, xxxhdpi: 192x192

4. **Place in Android Project:**
   - `app/src/main/res/mipmap-*/ic_launcher.png`
   - `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` (adaptive icon)

## Troubleshooting

### SVG Export Issues

**Problem:** Exported SVG has complex or invalid path data

**Solution:**
- Layer → Expand Stroke (convert strokes to fills)
- Layer → Flatten transforms
- Remove effects before export
- Simplify paths with fewer nodes

### Icons Look Blurry in Android

**Problem:** Vector icons appear blurred or pixelated

**Solution:**
- Ensure path coordinates align to pixel grid
- Avoid sub-pixel values (use integers)
- Check viewport size matches icon size
- Test with `android:width` and `android:height` set to 24dp

### Colors Don't Match Theme

**Problem:** Icon colors don't adapt to app theme

**Solution:**
- Use theme color references in Android XML
- `android:fillColor="?attr/colorPrimary"`
- Don't hardcode colors in path data

### Export Slices Not Working

**Problem:** Export slices generate unexpected output

**Solution:**
- Ensure slice bounds include all icon elements
- Check export format settings
- Verify output folder permissions
- Use Export Persona (Ctrl+/)

## Alternatives and Complementary Tools

### When to Use Affinity Designer
- Original icon design
- Complex vector editing
- Branding and logo work
- High-resolution asset creation

### When to Use SVG Path Editor
- Quick path tweaks
- Learning SVG syntax
- No software installation available
- See: [SVG Path Editor](/Slion/Fulguris/wiki/SVG-Path-Editor)

### When to Use Android Asset Studio
- Generating density variants
- Material icon customization
- Quick prototyping
- Website: https://romannurik.github.io/AndroidAssetStudio/

## Resources

### Learning Resources

**Official Affinity Designer Resources:**
- Official Workbook (PDF): https://affinity.serif.com/en-us/learn/
- Video Tutorials: https://affinity.serif.com/en-us/tutorials/designer/desktop/
- Community Forums: https://forum.affinity.serif.com/

**Icon Design:**
- Material Design Icons: https://material.io/design/iconography/
- Android Asset Studio: https://romannurik.github.io/AndroidAssetStudio/
- Icon Design Best Practices: https://developer.android.com/guide/practices/ui_guidelines/icon_design

### File Format Support

**Import:**
- SVG, PDF, EPS, AI (Adobe Illustrator)
- PSD (Photoshop), JPG, PNG, TIFF, GIF
- Raw camera formats

**Export:**
- SVG, PDF, EPS
- PNG, JPG, TIFF, GIF, WebP
- PSD (maintains layers)

## Related Documentation

- [SVG Path Editor](/Slion/Fulguris/wiki/SVG-Path-Editor) - Quick SVG path editing
- [Android Studio](/Slion/Fulguris/wiki/Android-Studio) - Previewing icons in context
- [Design/Icons](/Slion/Fulguris/wiki/Icons) - Icon design guidelines for Fulguris
- [Design/Branding](/Slion/Fulguris/wiki/Branding) - Brand identity guidelines

## External Links

- **Official Website:** https://affinity.serif.com/en-us/designer/
- **Desktop Version:** https://affinity.serif.com/en-us/designer/desktop/
- **iPad Version:** https://affinity.serif.com/en-us/designer/ipad/
- **Tutorials:** https://affinity.serif.com/en-us/tutorials/designer/
- **Community Forums:** https://forum.affinity.serif.com/
- **Asset Store:** https://affinity.serif.com/en-us/store/

---

**Last Updated:** December 21, 2025  
**Maintained by:** Fulguris Development Team
