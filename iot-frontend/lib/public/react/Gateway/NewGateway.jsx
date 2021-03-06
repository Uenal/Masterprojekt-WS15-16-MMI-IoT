var React = require('react')
  , request = require('superagent');

var NewGateway = React.createClass({

  propTypes: {
    user: React.PropTypes.object,
    handleNew: React.PropTypes.func
  },

  getInitialState: function() {
    return {
      name: '',
    };
  },

  handleChange: function() {
    this.setState({name: this.refs.name.value});
  },

  handleCancel: function() {
    this.props.handleNew();
  },

  handleSave: function() {
    var that = this;
    if (this.state.name) {
      var json = {
        name: this.state.name,
        owner: this.props.user.username
      };
      request
        .post('/gateway')
        .send(json)
        .end(function(err, res) {
          if (err) return console.log(err);
          //TODO: add warning
          //res.body === statuscode
          console.log(res);
          window.location.pathname = '/gateways';
        });
    }
  },

  render: function() {
    return (
      <div className='row'>
        <div className='large-8 large-centered columns'>
          <h2>Add a Gateway</h2>
          <div className='row'>
            <div className='large-4 columns'>
              Name
            </div>
            <div className='large-8 columns'>
              <input type='text'
                placeholder='cool gateway name'
                onChange={this.handleChange}
                ref='name' />
            </div>
          </div>
          <div className='row column' style={{textAlign: 'end'}}>
            <input type='button'
              className='button'
              value='Save'
              onClick={this.handleSave} />
            <input type='button'
              className='alert button'
              value='Cancel'
              style={{marginRight: 0}}
              onClick={this.handleCancel} />
          </div>
        </div>
      </div>
    );
  }

});

module.exports = NewGateway;
