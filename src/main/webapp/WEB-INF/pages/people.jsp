<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="js/jquery-1.10.0.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/es5-sham.min.js"></script>
    <script src="js/es5-shim.min.js"></script>
    <script src="js/polyfil.js"></script>
    <script src="js/react-with-addons.min.js"></script>
    <script src="js/JSXTransformer.js"></script>
    <script src="js/jquery-1.10.0.min.js"></script>
    <script src="js/showdown.min.js"></script>

    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-theme.min.css">
</head>
<body>
    <div id="peopleTableContent" class="row"></div>
    <div id="bestPersonContent" class="row"></div>
    <div id="addPersonContent" class="row"></div>
    <div id="content" class="row col-md-4 well"></div>
    <script>
        /**
         * @jsx React.DOM
         */
        var Title = React.createClass({displayName: 'Title',
            loadPeople: function() {
                $.ajax({
                    url: 'people/people',
                    dataType: 'json',
                    success: function(data) {
                        this.setState({data: data})
                    }.bind(this),
                    error: function(xhr, status, err) {
                        console.error(this.props.url, status, err.toString());
                    }.bind(this)
                });
            },
            getInitialState: function() {
                return {data: []};
            },
            componentWillMount: function() {
                this.loadPeople();
                setInterval(this.loadPeople, 2000);
            },
            render: function() {
                var tableRows = this.state.data.map(function (row) {
                    return PersonRow({name: row.name, age: row.age, sex: row.sex})
                });
                return (
                    React.DOM.div( {className:"col-md-4"},
                        React.DOM.h3(null, "Animals"),
                        React.DOM.table( {id:"friends", className:"table table-hover table-bordered table-responsive"},
                            React.DOM.thead(null,
                                React.DOM.tr(null,
                                    React.DOM.th(null, "Name"),
                                    React.DOM.th(null, "Age"),
                                    React.DOM.th(null, "Sex")
                                )
                            ),
                            React.DOM.tbody(null, tableRows)
                        )
                    )
                );
            }
        });

        var PersonRow = React.createClass({
            _onclick: function () {
                $.ajax({
                    url: "people/pick",
                    type: 'POST',
                    data: {personName: this.props.name},
                    success: function (data) {
                        $('#selectedPerson').html('You picked: ' + data.name + ' who is ' + data.sex + ' and is ' + data.age + ' years old');
                        $('#selectedPersonImage').attr('src', data.img.indexOf('http') != -1 ? data.img : 'img/' + data.img);
                    }
                })
            },
            render: function() {
                return (
                    React.DOM.tr({id: 'tr' + this.props.name, onClick: this._onclick},
                        React.DOM.td(null, this.props.name),
                        React.DOM.td(null, this.props.age),
                        React.DOM.td(null, this.props.sex)
                    )
                )
            }
        })

        React.renderComponent(
                Title({}),
                document.getElementById('peopleTableContent')
        );

        var maleData = [];
        var femaleData = [];
        var BestPerson = React.createClass({displayName: 'BestPerson',
            loadPeople: function() {
                $.ajax({
                    url: 'people/people',
                    dataType: 'json',
                    success: function(data) {
                        maleData = [];
                        femaleData = [];
                        for(var i = 0; i < data.length; i++) {
                            if (data[i].sex == 'Male') {
                                maleData.push(data[i]);
                            }
                            else if (data[i].sex == 'Female') {
                                femaleData.push(data[i]);
                            }
                        }
                        var people = {
                            maleData: maleData,
                            femaleData: femaleData
                        }
                        this.setState({data: people})
                    }.bind(this),
                    error: function(xhr, status, err) {
                        console.error(this.props.url, status, err.toString());
                    }.bind(this)
                });
            },
            getInitialState: function() {
                return {data: {}};
            },
            componentWillMount: function() {
                this.loadPeople();
                setInterval(this.loadPeople, 2000);
            },
            render: function() {
                var males = maleData.map( function(person) {
                    return ListEntry({name: person.name})
                });
                var females = femaleData.map( function(person) {
                    return ListEntry({name: person.name})
                });
                return (
                    React.DOM.div( {className:"col-md-4"},
                        React.DOM.a({
                            className: 'btn btn-primary',
                            id: 'downloadPeople',
                            type: 'button',
                            children: 'Download Animals!',
                            href: 'people/personFile'
                        }),
                        React.DOM.div({className: 'btn-group'},
                                React.DOM.button({id: 'personSelectorMale', type: 'button', className: 'btn btn-success dropdown-toggle', 'data-toggle': 'dropdown'}, "Male ", React.DOM.span({className: 'caret'})),
                                React.DOM.ul({className: 'dropdown-menu', role: 'menu'}, males)
                        ),
                        React.DOM.div({className: 'btn-group'},
                                React.DOM.button({id: 'personSelectorFemale', type: 'button', className: 'btn btn-warning dropdown-toggle', 'data-toggle': 'dropdown'}, "Female ", React.DOM.span({className: 'caret'})),
                                React.DOM.ul({className: 'dropdown-menu', role: 'menu'}, females)
                        ),
                        React.DOM.div( {id:'selectedPerson'}),
                        React.DOM.img( {id:'selectedPersonImage', style:{height: '400px'}})
                    )
                );
            }
        });

        var ListEntry = React.createClass({
            _onclick: function () {
                $.ajax({
                    url: "people/pick",
                    type: 'POST',
                    data: {personName: this.props.name},
                    success: function (data) {
                        $('#selectedPerson').html('You picked: ' + data.name + ' who is ' + data.sex + ' and is ' + data.age + ' years old');
                        $('#selectedPersonImage').attr('src', data.img.indexOf('http') != -1 ? data.img : 'img/' + data.img);
                    }
                })
            },
            render: function() {
                return (
                    React.DOM.li({id:'li' + this.props.name, onClick: this._onclick},
                            React.DOM.a(null, this.props.name)
                    )
                );
            }
        });

        React.renderComponent(
                BestPerson({}),
                document.getElementById('bestPersonContent')
        );

        var AddPerson = React.createClass({displayName: 'AddPerson',
            handleSubmit: function() {
                var name = this.refs.name.getDOMNode().value.trim();
                var age = this.refs.age.getDOMNode().value.trim();
                var sex = this.refs.sex.getDOMNode().value.trim();
                var img = this.refs.img.getDOMNode().value.trim();

                var person = {name: name, age: age, sex: sex, img: img};

                name = this.refs.name.getDOMNode().value = '';
                age = this.refs.age.getDOMNode().value = '';
                sex = this.refs.sex.getDOMNode().value = '';
                img = this.refs.img.getDOMNode().value = '';

                $.ajax({
                    url: 'people/addperson',
                    dataType: 'json',
                    type: 'POST',
                    data: JSON.stringify(person),
                    contentType: 'application/json',
                    mimeType: 'application/json',
                    success: function(data) {
                        alert('Successfully added new person!');
                    }.bind(this),
                    error: function(xhr, status, err) {
                        console.error(this.props.url, status, err.toString());
                    }.bind(this)
                });

                return false;
            },
            render: function() {
                return (
                        React.DOM.div( {className:"col-md-4 button-group"},
                                React.DOM.form({className:"addPersonForm form-horizontal", onSubmit:this.handleSubmit},
                                        React.DOM.h3(null, 'Add Animal'),
                                        React.DOM.div({className: 'input-group'},
                                            React.DOM.span({className: 'input-group-addon'}, 'Name:'),
                                            React.DOM.input({id: 'addFullName', type: 'text', className: 'form-control', placeholder: 'Full Name', ref: 'name'})),
                                        React.DOM.br(),
                                        React.DOM.div({className: 'input-group'},
                                                React.DOM.span({className: 'input-group-addon'}, 'Age:'),
                                                React.DOM.input({id: 'addAge', type: 'text', className: 'form-control', placeholder: 'Age', ref: 'age'})),
                                        React.DOM.br(),
                                        React.DOM.div({className: 'input-group'},
                                                React.DOM.span({className: 'input-group-addon'}, 'Sex:'),
                                                React.DOM.input({id: 'addSex', type: 'text', className: 'form-control', placeholder: 'Sex', ref: 'sex'})),
                                        React.DOM.br(),
                                        React.DOM.div({className: 'input-group'},
                                                React.DOM.span({className: 'input-group-addon'}, 'Image URL:'),
                                                React.DOM.input({id: 'addImg', type: 'text', className: 'form-control', placeholder: 'Image URL', ref: 'img'})),
                                        React.DOM.br(),
                                        React.DOM.button({className:'btn btn-primary'}, 'Add Person!')
                                )
                        )
                        );
            }
        });

        React.renderComponent(
                AddPerson({}),
                document.getElementById('addPersonContent')
        );

        var CommentBox = React.createClass({displayName: 'CommentBox',
            loadCommentsFromServer: function() {
                $.ajax({
                    url: this.props.get,
                    dataType: 'json',
                    success: function(data) {
                        this.setState({data: data});
                    }.bind(this),
                    error: function(xhr, status, err) {
                        console.error(this.props.url, status, err.toString());
                    }.bind(this)
                });
            },
            handleCommentSubmit: function(comment) {
                $.ajax({
                    url: this.props.set,
                    dataType: 'json',
                    type: 'POST',
                    data: JSON.stringify(comment),
                    contentType: 'application/json',
                    mimeType: 'application/json',
                    success: function(data) {
                        this.setState({data: data});
                    }.bind(this),
                    error: function(xhr, status, err) {
                        console.error(this.props.url, status, err.toString());
                    }.bind(this)
                });
            },
            getInitialState: function() {
                return {data: []};
            },
            componentWillMount: function() {
                this.loadCommentsFromServer();
                setInterval(this.loadCommentsFromServer, this.props.pollInterval);
            },
            render: function() {
                return (
                        React.DOM.div( {className:"commentBox"},
                                React.DOM.h1(null, "Comments"),
                                CommentList( {data:this.state.data} ),
                                CommentForm(
                                        {onCommentSubmit:this.handleCommentSubmit}
                                )
                        )
                        );
            }
        });

        var CommentList = React.createClass({
            render: function() {
                var commentNodes = this.props.data.map(function(comment) {
                    return Comment( {author: comment.author}, comment.text);
                });
                return (
                    React.DOM.div({className: 'commentList'},
                        commentNodes
                    )
                );
            }
        });

        var CommentForm = React.createClass({displayName: 'CommentForm',
            handleSubmit: function() {
                var author = this.refs.author.getDOMNode().value.trim();
                var text = this.refs.text.getDOMNode().value.trim();
                this.props.onCommentSubmit({author: author, text: text});
                this.refs.author.getDOMNode().value = '';
                this.refs.text.getDOMNode().value = '';
                return false;
            },
            render: function() {
                return (
                    React.DOM.form( {className:"commentForm form-horizontal", onSubmit:this.handleSubmit},
                        React.DOM.div({className: 'input-group'},
                            React.DOM.span({className: 'input-group-addon'}, "Name"),
                            React.DOM.input( {id: 'commentName', className: 'form-control', type:"text", placeholder:"Your name", ref:"author"} )
                        ),
                        React.DOM.div({className: 'input-group'},
                            React.DOM.span({className: 'input-group-addon'}, "Comment"),
                            React.DOM.input( {id: 'commentText', className: 'form-control', type:"text", placeholder:"Say something...", ref:"text"} )
                        ),
                        React.DOM.div({className: 'input-group'},
                            React.DOM.button( {className: 'btn btn-primary'}, "Submit!")
                        )
                    )
                );
            }
        });

        var converter = new Showdown.converter();

        var Comment = React.createClass({
            render: function() {
                var rawMarkup = converter.makeHtml(this.props.children.toString())
                return (
                    React.DOM.div({className: 'comment'},
                        React.DOM.h3({className: 'commentAuthor'}, this.props.author),
                        React.DOM.span({className: 'commentText', dangerouslySetInnerHTML: { __html: rawMarkup}})
                    )
                );
            }
        });

        React.renderComponent(
          CommentBox({get:'people/getComments', set:'people/addComment', pollInterval:2000}),
          document.getElementById('content')
        );

    </script>
    </body>
</html>