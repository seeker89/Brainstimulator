$(window).bind "load", () ->
  console.log "mettre en place un gravatar"
  $("#gravatar-container").html('<img src="http://www.gravatar.com/avatar/'+hex_md5($("#gravatar-container").html())+'" />')










#
# * A JavaScript implementation of the RSA Data Security, Inc. MD5 Message
# * Digest Algorithm, as defined in RFC 1321.
# * Version 2.2 Copyright (C) Paul Johnston 1999 - 2009
# * Other contributors: Greg Holt, Andrew Kepert, Ydnar, Lostinet
# * Distributed under the BSD License
# * See http://pajhome.org.uk/crypt/md5 for more info.
# 

#
# * Configurable variables. You may need to tweak these to be compatible with
# * the server-side, but the defaults work in most cases.
# 
# hex output format. 0 - lowercase; 1 - uppercase        
# base-64 pad character. "=" for strict RFC compliance   

#
# * These are the functions you'll usually want to call
# * They take string arguments and return either hex or base-64 encoded strings
# 
hex_md5 = (s) ->
  rstr2hex rstr_md5(str2rstr_utf8(s))
b64_md5 = (s) ->
  rstr2b64 rstr_md5(str2rstr_utf8(s))
any_md5 = (s, e) ->
  rstr2any rstr_md5(str2rstr_utf8(s)), e
hex_hmac_md5 = (k, d) ->
  rstr2hex rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d))
b64_hmac_md5 = (k, d) ->
  rstr2b64 rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d))
any_hmac_md5 = (k, d, e) ->
  rstr2any rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d)), e

#
# * Perform a simple self-test to see if the VM is working
# 
md5_vm_test = ->
  hex_md5("abc").toLowerCase() is "900150983cd24fb0d6963f7d28e17f72"

#
# * Calculate the MD5 of a raw string
# 
rstr_md5 = (s) ->
  binl2rstr binl_md5(rstr2binl(s), s.length * 8)

#
# * Calculate the HMAC-MD5, of a key and some data (raw strings)
# 
rstr_hmac_md5 = (key, data) ->
  bkey = rstr2binl(key)
  bkey = binl_md5(bkey, key.length * 8)  if bkey.length > 16
  ipad = Array(16)
  opad = Array(16)
  i = 0

  while i < 16
    ipad[i] = bkey[i] ^ 0x36363636
    opad[i] = bkey[i] ^ 0x5C5C5C5C
    i++
  hash = binl_md5(ipad.concat(rstr2binl(data)), 512 + data.length * 8)
  binl2rstr binl_md5(opad.concat(hash), 512 + 128)

#
# * Convert a raw string to a hex string
# 
rstr2hex = (input) ->
  try
    hexcase
  catch e
    hexcase = 0
  hex_tab = (if hexcase then "0123456789ABCDEF" else "0123456789abcdef")
  output = ""
  x = undefined
  i = 0

  while i < input.length
    x = input.charCodeAt(i)
    output += hex_tab.charAt((x >>> 4) & 0x0F) + hex_tab.charAt(x & 0x0F)
    i++
  output

#
# * Convert a raw string to a base-64 string
# 
rstr2b64 = (input) ->
  try
    b64pad
  catch e
    b64pad = ""
  tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
  output = ""
  len = input.length
  i = 0

  while i < len
    triplet = (input.charCodeAt(i) << 16) | ((if i + 1 < len then input.charCodeAt(i + 1) << 8 else 0)) | ((if i + 2 < len then input.charCodeAt(i + 2) else 0))
    j = 0

    while j < 4
      if i * 8 + j * 6 > input.length * 8
        output += b64pad
      else
        output += tab.charAt((triplet >>> 6 * (3 - j)) & 0x3F)
      j++
    i += 3
  output

#
# * Convert a raw string to an arbitrary string encoding
# 
rstr2any = (input, encoding) ->
  divisor = encoding.length
  i = undefined
  j = undefined
  q = undefined
  x = undefined
  quotient = undefined
  
  # Convert to an array of 16-bit big-endian values, forming the dividend 
  dividend = Array(Math.ceil(input.length / 2))
  i = 0
  while i < dividend.length
    dividend[i] = (input.charCodeAt(i * 2) << 8) | input.charCodeAt(i * 2 + 1)
    i++
  
  #
  #   * Repeatedly perform a long division. The binary array forms the dividend,
  #   * the length of the encoding is the divisor. Once computed, the quotient
  #   * forms the dividend for the next step. All remainders are stored for later
  #   * use.
  #   
  full_length = Math.ceil(input.length * 8 / (Math.log(encoding.length) / Math.log(2)))
  remainders = Array(full_length)
  j = 0
  while j < full_length
    quotient = Array()
    x = 0
    i = 0
    while i < dividend.length
      x = (x << 16) + dividend[i]
      q = Math.floor(x / divisor)
      x -= q * divisor
      quotient[quotient.length] = q  if quotient.length > 0 or q > 0
      i++
    remainders[j] = x
    dividend = quotient
    j++
  
  # Convert the remainders to the output string 
  output = ""
  i = remainders.length - 1
  while i >= 0
    output += encoding.charAt(remainders[i])
    i--
  output

#
# * Encode a string as utf-8.
# * For efficiency, this assumes the input is valid utf-16.
# 
str2rstr_utf8 = (input) ->
  output = ""
  i = -1
  x = undefined
  y = undefined
  while ++i < input.length
    
    # Decode utf-16 surrogate pairs 
    x = input.charCodeAt(i)
    y = (if i + 1 < input.length then input.charCodeAt(i + 1) else 0)
    if 0xD800 <= x and x <= 0xDBFF and 0xDC00 <= y and y <= 0xDFFF
      x = 0x10000 + ((x & 0x03FF) << 10) + (y & 0x03FF)
      i++
    
    # Encode output as utf-8 
    if x <= 0x7F
      output += String.fromCharCode(x)
    else if x <= 0x7FF
      output += String.fromCharCode(0xC0 | ((x >>> 6) & 0x1F), 0x80 | (x & 0x3F))
    else if x <= 0xFFFF
      output += String.fromCharCode(0xE0 | ((x >>> 12) & 0x0F), 0x80 | ((x >>> 6) & 0x3F), 0x80 | (x & 0x3F))
    else output += String.fromCharCode(0xF0 | ((x >>> 18) & 0x07), 0x80 | ((x >>> 12) & 0x3F), 0x80 | ((x >>> 6) & 0x3F), 0x80 | (x & 0x3F))  if x <= 0x1FFFFF
  output

#
# * Encode a string as utf-16
# 
str2rstr_utf16le = (input) ->
  output = ""
  i = 0

  while i < input.length
    output += String.fromCharCode(input.charCodeAt(i) & 0xFF, (input.charCodeAt(i) >>> 8) & 0xFF)
    i++
  output
str2rstr_utf16be = (input) ->
  output = ""
  i = 0

  while i < input.length
    output += String.fromCharCode((input.charCodeAt(i) >>> 8) & 0xFF, input.charCodeAt(i) & 0xFF)
    i++
  output

#
# * Convert a raw string to an array of little-endian words
# * Characters >255 have their high-byte silently ignored.
# 
rstr2binl = (input) ->
  output = Array(input.length >> 2)
  i = 0

  while i < output.length
    output[i] = 0
    i++
  i = 0

  while i < input.length * 8
    output[i >> 5] |= (input.charCodeAt(i / 8) & 0xFF) << (i % 32)
    i += 8
  output

#
# * Convert an array of little-endian words to a string
# 
binl2rstr = (input) ->
  output = ""
  i = 0

  while i < input.length * 32
    output += String.fromCharCode((input[i >> 5] >>> (i % 32)) & 0xFF)
    i += 8
  output

#
# * Calculate the MD5 of an array of little-endian words, and a bit length.
# 
binl_md5 = (x, len) ->
  
  # append padding 
  x[len >> 5] |= 0x80 << ((len) % 32)
  x[(((len + 64) >>> 9) << 4) + 14] = len
  a = 1732584193
  b = -271733879
  c = -1732584194
  d = 271733878
  i = 0

  while i < x.length
    olda = a
    oldb = b
    oldc = c
    oldd = d
    a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936)
    d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586)
    c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819)
    b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330)
    a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897)
    d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426)
    c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341)
    b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983)
    a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416)
    d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417)
    c = md5_ff(c, d, a, b, x[i + 10], 17, -42063)
    b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162)
    a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682)
    d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101)
    c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290)
    b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329)
    a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510)
    d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632)
    c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713)
    b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302)
    a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691)
    d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083)
    c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335)
    b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848)
    a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438)
    d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690)
    c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961)
    b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501)
    a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467)
    d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784)
    c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473)
    b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734)
    a = md5_hh(a, b, c, d, x[i + 5], 4, -378558)
    d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463)
    c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562)
    b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556)
    a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060)
    d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353)
    c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632)
    b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640)
    a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174)
    d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222)
    c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979)
    b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189)
    a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487)
    d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835)
    c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520)
    b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651)
    a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844)
    d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415)
    c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905)
    b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055)
    a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571)
    d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606)
    c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523)
    b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799)
    a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359)
    d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744)
    c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380)
    b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649)
    a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070)
    d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379)
    c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259)
    b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551)
    a = safe_add(a, olda)
    b = safe_add(b, oldb)
    c = safe_add(c, oldc)
    d = safe_add(d, oldd)
    i += 16
  Array a, b, c, d

#
# * These functions implement the four basic operations the algorithm uses.
# 
md5_cmn = (q, a, b, x, s, t) ->
  safe_add bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b
md5_ff = (a, b, c, d, x, s, t) ->
  md5_cmn (b & c) | ((~b) & d), a, b, x, s, t
md5_gg = (a, b, c, d, x, s, t) ->
  md5_cmn (b & d) | (c & (~d)), a, b, x, s, t
md5_hh = (a, b, c, d, x, s, t) ->
  md5_cmn b ^ c ^ d, a, b, x, s, t
md5_ii = (a, b, c, d, x, s, t) ->
  md5_cmn c ^ (b | (~d)), a, b, x, s, t

#
# * Add integers, wrapping at 2^32. This uses 16-bit operations internally
# * to work around bugs in some JS interpreters.
# 
safe_add = (x, y) ->
  lsw = (x & 0xFFFF) + (y & 0xFFFF)
  msw = (x >> 16) + (y >> 16) + (lsw >> 16)
  (msw << 16) | (lsw & 0xFFFF)

#
# * Bitwise rotate a 32-bit number to the left.
# 
bit_rol = (num, cnt) ->
  (num << cnt) | (num >>> (32 - cnt))
hexcase = 0
b64pad = ""