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

#pragma comment (lib, "jvm.lib")
#pragma comment (lib, "jawt.lib")

#include <jni.h>
#include <stdio.h>
#include <string.h>

#if defined(WIN32)
#include <io.h>
#endif

#if defined(LINUX)
#include <dirent.h>
#include "linux.h"
#endif

#include "jni_core.h"


JavaVM *jvm = NULL;
JNIEnv *env = NULL;


int jni_jvm_create( const char* classpath )
{
	JavaVMInitArgs vm_args;
	JavaVMOption options[3];

	if( jvm != NULL ) return -1;


	char clspath[2048] = "-Djava.class.path=";
	char jarpath[512];

	strcpy( jarpath, classpath );
	for( int i=strlen(jarpath)-1; i>=0; i-- )
		if( jarpath[i] == '/' ) { jarpath[i+1] = 0; break; }

#if defined(WIN32)

	_finddata_t finddata;
	int hfind = _findfirst(classpath, &finddata);
	if( hfind < 0 ) return -2;

	do 
	{
		strcat( clspath, jarpath );
		strcat( clspath, finddata.name );
		strcat( clspath, ";" );
	} while ( !_findnext(hfind, &finddata) );
	_findclose( hfind );
#endif

#if defined(LINUX)
	DIR *dir = opendir(jarpath);
	struct dirent entry;
	struct dirent* entryPtr = NULL;

	readdir_r(dir, &entry, &entryPtr);
	if(entryPtr == NULL) return -2;

	while(entryPtr != NULL)
	{
		if(strncmp(entry.d_name, ".", PATH_MAX) ==0 ||
			strncmp(entry.d_name, "..", PATH_MAX) ==0)
		{
			readdir_r(dir, &entry, &entryPtr);
			continue;
		}

		int len = strlen(entry.d_name);

		if(strcmp(strlwr(&(entry.d_name[len-4])), ".jar") != 0)
		{
			readdir_r(dir, &entry, &entryPtr);
			continue;
		}

		/*char resolved[PATH_MAX];
		char temp[PATH_MAX] = "";
		strcat( temp, jarpath );
		strcat( temp, entry.d_name );
		realpath(temp, resolved);

		strcat( clspath, resolved );*/
		strcat( clspath, jarpath );
		strcat( clspath, entry.d_name );
		strcat( clspath, ":" );

		readdir_r(dir, &entry, &entryPtr);
	}

	closedir(dir);
#endif

	clspath[ strlen(clspath)-1 ] = 0;

	options[0].optionString = clspath;
	options[1].optionString = "-verbose:gc";
	options[2].optionString = "-Djava.library.path=./plugins";
	//options[3].optionString = "-Djava.compiler=NONE";

	vm_args.version = JNI_VERSION_1_6;
	vm_args.options = options;
	vm_args.nOptions = sizeof(options) / sizeof(JavaVMOption);
	vm_args.ignoreUnrecognized = JNI_FALSE;

	jint res = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
	if (res < 0) return -3;

	return 0;
}

int jni_jvm_processException()
{
	if( !env->ExceptionCheck() ) return 1;

	jthrowable throwable = env->ExceptionOccurred();
	jclass throwableCls = env->GetObjectClass(throwable);
	jmethodID printStackTraceId = env->GetMethodID(throwableCls, "printStackTrace", "()V");
	if( !printStackTraceId ) return -4;

	env->CallVoidMethod( throwable, printStackTraceId );
	env->ExceptionClear();

	return 0;
}

int jni_jvm_newobject( jclass jcls, jobject *pjobj )
{
	if( !jvm ) return -1;

	jmethodID jmid = env->GetMethodID(jcls, "<init>", "()V");
	if( !jmid ) return -2;

	*pjobj = env->NewObject( jcls, jmid );
	if( !*pjobj )
	{
		jni_jvm_processException();
		return -3;
	}

	return 0;
}

int jni_jvm_destroy()
{
	if( !jvm ) return -1;

	if ( env->ExceptionOccurred() ) env->ExceptionDescribe();
	jvm->DestroyJavaVM();

	env = NULL;
	jvm = NULL;
	return 0;
}
