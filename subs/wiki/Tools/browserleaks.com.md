# BrowserLeaks

**URL:** https://browserleaks.com

## Overview

BrowserLeaks is a comprehensive suite of online tools for testing browser privacy and security features. We use it to validate various features of Fulguris Web Browser, including geolocation, client hints, user agent detection, and other browser fingerprinting vectors.

## Key Testing Areas

### Geolocation
- **URL:** https://browserleaks.com/geo
- Tests HTML5 Geolocation API
- Validates location permission prompts
- Confirms location blocking/allowing functionality
- Tests domain-specific location settings

### Client Hints
- **URL:** https://browserleaks.com/client-hints
- Validates Client Hints headers (Sec-CH-UA, Sec-CH-UA-Mobile, etc.)
- Tests Accept-CH response headers
- Requires HTTPS for Client Hints to work
- Useful for testing user agent customization

### User Agent
- **URL:** https://browserleaks.com/ua
- Displays current user agent string
- Shows browser engine, platform, and version information
- Tests desktop/mobile user agent switching

### IP Address
- **URL:** https://browserleaks.com/ip
- Shows client IP address and location
- Shows request headers
- Tests proxy/VPN functionality
- Validates WebRTC IP leak protection

### Canvas Fingerprinting
- **URL:** https://browserleaks.com/canvas
- Tests canvas fingerprinting resistance
- Shows canvas rendering characteristics

### WebRTC
- **URL:** https://browserleaks.com/webrtc
- Tests WebRTC functionality
- Checks for IP address leaks via WebRTC
- Validates WebRTC enable/disable settings

### JavaScript
- **URL:** https://browserleaks.com/javascript
- Comprehensive JavaScript feature detection
- Tests which JavaScript APIs are available
- Validates JavaScript blocking settings

### Fonts
- **URL:** https://browserleaks.com/fonts
- Detects installed system fonts
- Tests font fingerprinting resistance

### Screen Resolution
- **URL:** https://browserleaks.com/screen
- Shows screen dimensions and color depth
- Tests screen fingerprinting

## Common Test Scenarios

### Testing Geolocation Features
1. Navigate to https://browserleaks.com/geo
2. Click "Show my location"
3. Verify permission dialog appears
4. Test Allow/Deny functionality
5. Verify domain-specific settings work correctly
6. Test location permission reset

### Testing Client Hints
1. Navigate to https://browserleaks.com/client-hints
2. Verify page loads over HTTPS (required for Client Hints)
3. Check which Client Hints headers are sent
4. Test with different user agent settings
5. Validate Accept-CH headers are processed correctly

### Testing User Agent Customization
1. Navigate to https://browserleaks.com/ua
2. Change user agent setting in Fulguris
3. Refresh page and verify new user agent is displayed
4. Test desktop mode vs mobile mode
5. Verify consistency across different pages

### Testing Privacy Features
1. Enable various privacy settings in Fulguris
2. Visit multiple BrowserLeaks test pages
3. Verify expected information is blocked/modified
4. Compare fingerprint before/after settings changes

## Integration with Development

### Feature Development
- Use BrowserLeaks to validate new privacy features
- Test geolocation permissions dialog behavior
- Verify client hints implementation
- Validate user agent customization

### Testing Workflow
1. Make changes to privacy/location features
2. Build and install Fulguris
3. Run through relevant BrowserLeaks tests
4. Document any discrepancies
5. Verify fixes with follow-up tests

### Debugging
- Useful for isolating browser behavior issues
- Provides detailed technical information
- Helps identify fingerprinting vectors
- Validates header transmission

## Notes

- **HTTPS Required:** Some features (like Client Hints) only work over HTTPS
- **Permission Prompts:** Geolocation tests trigger permission dialogs - useful for testing permission UI
- **Fingerprinting:** Many tests intentionally expose fingerprinting vectors to help developers understand privacy implications
- **Reference Implementation:** Compare Fulguris behavior against other browsers on the same tests

## Related Documentation

- [Geolocation Implementation](/Slion/Fulguris/wiki/Geolocation)
- [Client Hints Support](/Slion/Fulguris/wiki/ClientHints)
- [Privacy Features](/Slion/Fulguris/wiki/Privacy)

## External Links

- [BrowserLeaks GitHub](https://github.com/BrowserLeaks/browserleaks)
- [BrowserLeaks Documentation](https://browserleaks.com/about)
