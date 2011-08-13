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

#include "../../Dependencies/sampplugin/amx/amx.h"
#include "../../Dependencies/sampplugin/plugincommon.h"

#include <string.h>


typedef int (*amx_native_t)(AMX *amx, cell* params);


extern AMX *pAMX;
extern void *pAMXFunctions;
extern AMX_NATIVE_INFO CallbackNatives[];


inline amx_native_t amx_FindNative( AMX *amx, const char *func )
{
	if( !amx )						return NULL;

	int index;
	amx_FindNative( amx, func, &index);
	if( index == 2147483647 )		return NULL;

	AMX_HEADER *hdr = (AMX_HEADER*)amx->base;
	AMX_FUNCSTUB *funcstub = (AMX_FUNCSTUB*)( (char*)(hdr)+ hdr->natives + hdr->defsize*index );

	return (amx_native_t)funcstub->address;
}

inline cell amx_Allot( AMX *amx, int len, cell **phys )
{
	cell amx_str;
	amx_Allot( amx, len, &amx_str, phys );

	return amx_str;
}

inline char* amx_GetString( AMX *amx, const cell str, char *dest, int size )
{
	cell *phys;

	amx_GetAddr( amx, str, &phys );
	amx_GetString( dest, phys, 0, size );

	return dest;
}

inline cell amx_NewString( AMX *amx, const char* str, int len=-1 )
{
	cell amx_str, *amx_str_phys;
	if( len<0 ) len = strlen(str) + 1;

	amx_Allot( amx, len, &amx_str, &amx_str_phys );
	amx_SetString( amx_str_phys, str, 0, 0, len );

	return amx_str;
}
