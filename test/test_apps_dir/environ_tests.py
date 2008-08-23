# -*- coding: windows-1252 -*-

"""
	A variety of app callables used to test the WSGI environment.
"""

def test_echo_wsgi_env(environ, start_response):
	writer = start_response("200 OK", [])
	output_dict = {}
	for k in environ["QUERY_STRING"].split(';'):
		output_dict[k] = environ[k]
	writer(repr(output_dict))
	return []

def test_env_is_dict(environ, start_response):
	writer = start_response("200 OK", [])
	if type(environ) is type({}):
		writer("true")
	else:
		writer("false")
	return []

def test_env_is_mutable(environ, start_response):
	writer = start_response("200 OK", [])
	try:
		environ['some_key'] = 'some value'
		writer("true")
	except:
		writer("false")
	return []

def test_env_contains_request_method(environ, start_response):
	writer = start_response("200 OK", [])
	try:
		writer(environ['REQUEST_METHOD'])
	except KeyError, k:
		writer(str(k))
	return []

def test_env_script_name_path_info(environ, start_response):
	writer = start_response("200 OK", [])
	writer("%s:::%s" % (environ['SCRIPT_NAME'], environ['PATH_INFO']))
	return []

def test_env_query_string(environ, start_response):
	writer = start_response("200 OK", [])
	writer(environ['QUERY_STRING'])
	return []

