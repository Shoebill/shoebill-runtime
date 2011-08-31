/**
 * Copyright (C) 2011 MK124
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "encoding.h"


#if defined(WIN32)
#include <Windows.h>

int mbs2wcs( unsigned int codepage, const char* src, int srclen, unsigned short* dst, int dstlen )
{
	return MultiByteToWideChar(codepage, MB_COMPOSITE, src, srclen, (LPWSTR)dst, dstlen-1);
}

int wcs2mbs( unsigned int codepage, const unsigned short* src, int srclen, char* dst, int dstlen, bool* usedDefChar )
{
	BOOL usedDefaultChar = FALSE;
	int ret = WideCharToMultiByte(codepage, WC_COMPOSITECHECK, (LPCWSTR)src, srclen, dst, dstlen-1, "?", &usedDefaultChar);
	
	if( usedDefaultChar && (codepage == 950 || codepage == 932) )	// BIG5(TW) or SHIFT-JIS(JP)
	{
		if( srclen<0 ) srclen = wcslen((LPCWSTR)src);

		wchar_t* convsrc = new wchar_t[srclen+1];
		int rett = LCMapStringW( 0x804, LCMAP_TRADITIONAL_CHINESE, (LPCWSTR)src, srclen, convsrc, srclen ) ;

		ret = WideCharToMultiByte(codepage, WC_COMPOSITECHECK, convsrc, srclen, dst, dstlen-1, "?", &usedDefaultChar);
		delete[] convsrc;
	}

	dst[ret] = 0;
	if(usedDefChar) *usedDefChar = usedDefaultChar!=FALSE;
	return ret;
}

#endif

//----------------------------------------------------------

#if defined(LINUX) || defined(FREEBSD) || defined(__FreeBSD__) || defined(__OpenBSD__)
#include <iconv.h>

int mbs2wcs( unsigned int codepage, const char* src, int srclen, unsigned short* dst, int dstlen )
{
	size_t inbytesleft = srclen, outbytesleft = (dstlen-1)*sizeof(unsigned short);
	char *in = (char*)src, *out = (char*)dst;

	int value = 1;

	iconv_t cd = iconv_open("UTF-16LE", "BIG5");
	iconvctl( cd, ICONV_SET_TRANSLITERATE, &value);
	iconvctl( cd, ICONV_SET_DISCARD_ILSEQ, &value);
	iconv( cd, &in, &inbytesleft, &out, &outbytesleft );
	iconv_close( cd );

	int len = dstlen-(outbytesleft/sizeof(unsigned short));
	dst[len] = 0;
	return len;
}

int wcs2mbs( unsigned int codepage, const unsigned short* src, int srclen, char* dst, int dstlen, bool* usedDefChar )
{
	size_t inbytesleft = srclen*sizeof(unsigned short), outbytesleft = dstlen-1;
	char *in = (char*)src, *out = (char*)dst;

	int value = 1;

	iconv_t cd = iconv_open("BIG5", "UTF-16LE");
	iconvctl( cd, ICONV_SET_TRANSLITERATE, &value);
	iconvctl( cd, ICONV_SET_DISCARD_ILSEQ, &value);
	iconv( cd, &in, &inbytesleft, &out, &outbytesleft );
	iconv_close( cd );

	int len = dstlen-outbytesleft;
	dst[len] = 0;
	return len;
}

#endif
