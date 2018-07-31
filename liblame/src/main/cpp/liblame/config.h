#ifndef LAME_CONFIG_H
#define LAME_CONFIG_H

/* Define to 1 if you have the <errno.h> header file. */
#define HAVE_ERRNO_H 1

/* Define to 1 if you have the <fcntl.h> header file. */
#define HAVE_FCNTL_H 1

/* add ieee754_float32_t type */
/* #undef HAVE_IEEE754_FLOAT32_T */
#ifndef HAVE_IEEE754_FLOAT32_T
	typedef float ieee754_float32_t;
#endif

#define STDC_HEADERS 1

#endif /* LAME_CONFIG_H */
